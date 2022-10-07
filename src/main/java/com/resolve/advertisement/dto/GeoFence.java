package com.resolve.advertisement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoFence {

    private Long Id;
    private Double latitude;
    private Double longitude;
    private Double radius;

}
