package com.resolve.advertisement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GeoFenceDto {
    private Integer code;
    private String message;
    private Integer status;
    private String requestId;
    private List<GeoFence> data;
}
