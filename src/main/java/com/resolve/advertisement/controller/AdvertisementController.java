package com.resolve.advertisement.controller;

import com.resolve.advertisement.dto.ResponseDto;
import com.resolve.advertisement.entity.AdvertisementEntity;
import com.resolve.advertisement.repository.service.AdvertisementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/advertisement/api")
public class AdvertisementController {

    @Autowired
    AdvertisementService advertisementService;

    @PostMapping("/createAdvertisement")
    public ResponseDto addAdvertisement(@Valid @RequestBody AdvertisementEntity request) {
        log.info("creating advertising for request {}", request);
        return advertisementService.addAdvertisement(request);
    }

    @DeleteMapping("deleteAdvertisement" + "/{advertisingId}")
    public ResponseDto deleteAdvertisement(@PathVariable final Long advertisingId) {
        log.info("deleting advertising for request {}", advertisingId);
        return advertisementService.deleteAdvertisement(advertisingId);
    }

    @PutMapping("updateAdvertisement")
    public ResponseDto updateAdvertisement(@Valid @RequestBody AdvertisementEntity request) {
        log.info("updating advertising for request {}", request);
        return advertisementService.updateAdvertisement(request);
    }

   @GetMapping("/getAdvertisement")
    public ResponseDto getAdvertisement(@RequestParam final Double latitude,@RequestParam final Double longitude){
        log.info("getting advertising for request lat {} long {}",latitude,longitude);
        return advertisementService.getAdvertisingInGeoLocation(latitude,longitude);
    }

}

