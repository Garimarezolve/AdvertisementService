package com.resolve.advertisement.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "advertisement_geo_mapping")
@NoArgsConstructor
public class AdvertisementGeoFenceMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long geoId;
    private Long addId;

    public AdvertisementGeoFenceMapping(Long geoId, Long addId) {
        super();
        this.geoId = geoId;
        this.addId = addId;
    }
}
