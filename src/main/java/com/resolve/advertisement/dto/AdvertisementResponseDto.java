package com.resolve.advertisement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdvertisementResponseDto extends  GeoFence {
    private List<AdvertisementDto> advertisementList = new ArrayList<>();
}
