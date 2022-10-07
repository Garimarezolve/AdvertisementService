package com.resolve.advertisement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resolve.advertisement.ApplicationConstantTest;
import com.resolve.advertisement.dto.GeoFenceDto;
import com.resolve.advertisement.dto.ResponseDto;
import com.resolve.advertisement.entity.AdvertisementEntity;
import com.resolve.advertisement.entity.AdvertisementGeoFenceMapping;
import com.resolve.advertisement.repository.AdvertisementGeoFenceMappingRepository;
import com.resolve.advertisement.repository.AdvertisementRepository;
import com.resolve.advertisement.repository.service.AdvertisementService;
import com.resolve.advertisement.utils.DistanceCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdvertisementServiceTest {
    @InjectMocks
    AdvertisementService advertisementService;

    @Mock
    AdvertisementRepository advertisementRepository;
    @Mock
    AdvertisementGeoFenceMappingRepository advertisementGeoFenceMappingRepository;
    AdvertisementEntity advertisement= new AdvertisementEntity();
    @Mock
    RestTemplate restTemplate;
    @Mock
    DistanceCalculator distanceCalculator;
    List<AdvertisementEntity> advertisements= new ArrayList<AdvertisementEntity>();
    List<AdvertisementGeoFenceMapping>advertisementGeoFenceMappings= new ArrayList<AdvertisementGeoFenceMapping>();

    @BeforeEach
    public  void  setUp(){
        AdvertisementEntity advertisement= new AdvertisementEntity();
        advertisement.setAddId(102L);
        advertisement.setHref("http://www.google.com");
        advertisement.setLongitude(12.0);
        advertisement.setLatitude(11.0);
        advertisement.setAdvertisementName("Test");
        advertisement.setUpdatedAt(new Date());
        advertisement.setCreatedAt(new Date());
    }

    @Test
     void createAdvertisement() throws  Exception{
        ObjectMapper mapper= new ObjectMapper();
        HttpHeaders headers= new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        when(restTemplate.exchange(advertisement.getHref(), HttpMethod.GET, entity, String.class)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        given(advertisementRepository.save(Mockito.any())).willReturn(advertisement);
        GeoFenceDto geoFencesDto = mapper.readValue(ApplicationConstantTest.GEO_RESPONSE, GeoFenceDto.class);
        when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(ResponseEntity.ok(geoFencesDto));
        given(distanceCalculator.checkInsideGeoLocation(Mockito.any(),Mockito.any(),Mockito.any())).willReturn(true);
        ResponseDto advertisingModelResponse = advertisementService.addAdvertisement(advertisement);
        Assertions.assertEquals(200, advertisingModelResponse.getCode());
    }

    @Test
    void createAdvertising_invalidUrl() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        when(restTemplate.exchange(advertisement.getHref(), HttpMethod.GET, entity, String.class)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        ResponseDto advertisingModelResponse = advertisementService.addAdvertisement(advertisement);
        Assertions.assertEquals(424, advertisingModelResponse.getCode());
    }
    void updateAdvertising() {
        advertisement.setLatitude(11.23);
        given(advertisementRepository.findById(Mockito.any())).willReturn(Optional.of(advertisement));
        given(advertisementRepository.save(Mockito.any())).willReturn(advertisement);
        ResponseDto advertisingModelResponse = advertisementService.updateAdvertisement(advertisement);
        Assertions.assertEquals(200, advertisingModelResponse.getCode());
    }

    @Test
    void updateAdvertising_not_found() {
        advertisement.setAddId(201L);
        advertisement.setLongitude(11.23);
        given(advertisementRepository.findById(Mockito.any())).willReturn(Optional.empty());
        ResponseDto advertisingModelResponse = advertisementService.updateAdvertisement(advertisement);
        Assertions.assertEquals(404, advertisingModelResponse.getCode());
    }

    @Test
    void deleteAdvertising() {
        given(advertisementRepository.findById(Mockito.any())).willReturn(Optional.of(advertisement));
        ResponseDto advertisingModelResponse = advertisementService.deleteAdvertisement(advertisement.getAddId());
        Assertions.assertEquals(200, advertisingModelResponse.getCode());
    }
}
