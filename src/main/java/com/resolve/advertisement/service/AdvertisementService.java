package com.resolve.advertisement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resolve.advertisement.constant.ApplicationConstant;
import com.resolve.advertisement.dto.*;
import com.resolve.advertisement.entity.AdvertisementEntity;
import com.resolve.advertisement.entity.AdvertisementGeoFenceMapping;
import com.resolve.advertisement.repository.AdvertisementGeoFenceMappingRepository;
import com.resolve.advertisement.repository.AdvertisementRepository;
import com.resolve.advertisement.utils.DistanceCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdvertisementService {
    @Autowired
    AdvertisementRepository advertisementRepository;
    @Autowired
    AdvertisementGeoFenceMappingRepository advertisementGeoFenceMappingRepository;
    @Autowired
    RestTemplate restTemplate;
    @Value("${client.geo.baseUrl}")
    private String geoBaseClientURl;
    @Autowired
    DistanceCalculator distanceCalculator;

    /*
     * This function validate the href of advertisement
     */
    public boolean validateHrefUrl(String href) {
        boolean isValidate=false;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        int statusCode= restTemplate.exchange(href, HttpMethod.GET, entity, String.class).getStatusCodeValue();
        if (statusCode==200){
            isValidate=true;
        }
        return isValidate;
    }

    /*
     * This function returns the list of all client geofence HttpHeaders by using http call
     */
    public List<GeoFence> getGeoList() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        ResponseEntity<Object> response = restTemplate.getForEntity(geoBaseClientURl + "/Geos", Object.class);
        log.info("list of geofence is ", response);
        Object obj = response.getBody();
        ObjectMapper mapper = new ObjectMapper();
        GeoFenceDto geoFenceDto = mapper.convertValue(obj, GeoFenceDto.class);
        return geoFenceDto.getData();

    }

    /*
     * This function return the list of all geofence available at client system and
     * then checks the unique geolocation which are inside the radius
     */
    public ResponseDto getAdvertisement(Double latitude, Double longitude) {
        List<GeoFence> geoFencesList = checkInsideGeoFences(getGeoList(), latitude, longitude);
        return new SuccessResponseDto(geoFencesList);
    }

    private List<GeoFence> checkInsideGeoFences(List<GeoFence> geoFencesList, Double latitude, Double longitude) {
        List<GeoFence> insideGeoList = new ArrayList<>();
        geoFencesList.stream().forEach((loop) -> {
            log.info("checking lat and long inside Geo ", latitude, longitude, loop);
            if (distanceCalculator.checkInsideGeoLocation(loop, longitude, latitude)) {
                insideGeoList.add(loop);
            }

        });
        return insideGeoList;
    }

    @Transactional
    public ResponseDto addAdvertisement(AdvertisementEntity advertisement) {
        boolean isValidUrl = validateHrefUrl(advertisement.getHref());
        if (isValidUrl != true) {
            return new ErrorResponseDto(ApplicationConstant.HTTP_RESPONSE_ERROR_CODE_HREF,
                    "received unsuccessful status code " + isValidUrl +
                            " from " + advertisement.getHref());
        }
        AdvertisementEntity advertisementEntity = advertisementRepository.save(advertisement);
        List<GeoFence> geoFences = (List<GeoFence>) getAdvertisement(advertisement.getLatitude(),
                advertisement.getLongitude()).getData();
        Set<AdvertisementGeoFenceMapping> advertisingGeoFenceMappingSet = new HashSet<>();
        AdvertisementGeoFenceMapping advertisingGeoFenceMapping;
        if (geoFences != null && !geoFences.isEmpty()) {
            for (GeoFence loopGeoFence : geoFences) {
                advertisingGeoFenceMapping = new AdvertisementGeoFenceMapping(loopGeoFence.getId(),
                        advertisementEntity.getAddId());
                advertisingGeoFenceMappingSet.add(advertisingGeoFenceMapping);
            }
            advertisementGeoFenceMappingRepository.saveAll(advertisingGeoFenceMappingSet);
        }
        return new SuccessResponseDto(advertisementEntity);
    }


    /*
     * This function update the advertising data based on unique advertising id if
     * available in system
     */
    public ResponseDto updateAdvertisement(AdvertisementEntity request) {
        return advertisementRepository.findById(request.getAddId()).isPresent()
                ? new SuccessResponseDto(advertisementRepository.save(request))
                : new ErrorResponseDto(ApplicationConstant.NOT_FOUND, ApplicationConstant.NOT_FOUND_MSG);
    }


    public ResponseDto deleteAdvertisement(Long advertisingId) {
        if (advertisementRepository.findById(advertisingId).isPresent()) {
            advertisementRepository.deleteById(advertisingId);
            return new SuccessResponseDto(ApplicationConstant.HTTP_RESPONSE_SUCCESS_CODE);
        }
        return new ErrorResponseDto(ApplicationConstant.NOT_FOUND, ApplicationConstant.NOT_FOUND_MSG);
    }

    public ResponseDto getAdvertisingInGeoLocation(Double latitude, Double longitude){
        List<GeoFence> geoFencesList = checkInsideGeoFences(getGeoList(), latitude, longitude);
        List<Long> geoFenceIds = geoFencesList.parallelStream().map(GeoFence::getId).collect(Collectors.toList());
        List<AdvertisementGeoFenceMapping> advertisingGeoFenceMappings = advertisementGeoFenceMappingRepository.findByGeoFenceIds(geoFenceIds);
        List<Long> addIds = advertisingGeoFenceMappings.parallelStream().map(AdvertisementGeoFenceMapping::getAddId).collect(Collectors.toList());
        List<AdvertisementEntity> advertisementEntities= advertisementRepository.findAllById(addIds);
        Map<Long, AdvertisementEntity> advertisementEntitiesMap = advertisementEntities.stream().collect(Collectors.toMap(AdvertisementEntity::getAddId, Function.identity()));
        Map<Long, GeoFence> geoFenceMap = geoFencesList.stream().collect(Collectors.toMap(GeoFence::getId, Function.identity()));
        AdvertisementResponseDto responseDto;
        Map<Long, AdvertisementResponseDto> response = new HashMap<>();
        AdvertisementDto advertisementDto;
        for (AdvertisementGeoFenceMapping advertisingGeoFenceMapping : advertisingGeoFenceMappings) {
            AdvertisementEntity advertisementEntity = advertisementEntitiesMap.get(advertisingGeoFenceMapping.getAddId());
            GeoFence geoFence = geoFenceMap.get(advertisingGeoFenceMapping.getGeoId());
            if (response.get(advertisingGeoFenceMapping.getGeoId()) == null) {
                responseDto = new AdvertisementResponseDto();
                responseDto.setId(geoFence.getId());
                responseDto.setLongitude(geoFence.getLongitude());
                responseDto.setLatitude(geoFence.getLatitude());
                responseDto.setRadius(geoFence.getRadius());
                advertisementDto = new AdvertisementDto(advertisementEntity);
                double distance = distanceCalculator.haversineDistance(geoFence.getLongitude(), geoFence.getLatitude(),
                        advertisementEntity.getLongitude(), advertisementEntity.getLatitude());
                advertisementDto.setDistance(distance);
                responseDto.getAdvertisementList().add(advertisementDto);
                response.put(geoFence.getId(),responseDto);
            }else{
                AdvertisementResponseDto responseDto1 =  response.get(advertisingGeoFenceMapping.getGeoId());
                advertisementDto = new AdvertisementDto(advertisementEntity);
                double distance = distanceCalculator.haversineDistance(geoFence.getLongitude(),
                                geoFence.getLatitude(), advertisementEntity.getLongitude(), advertisementEntity.getLatitude());
                advertisementDto.setDistance(distance);
                responseDto1.getAdvertisementList().add(advertisementDto);
            }
        }

        return new SuccessResponseDto(response.values());
    }
}
