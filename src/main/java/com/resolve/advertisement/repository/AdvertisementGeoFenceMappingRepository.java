package com.resolve.advertisement.repository;

import com.resolve.advertisement.entity.AdvertisementGeoFenceMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementGeoFenceMappingRepository extends JpaRepository<AdvertisementGeoFenceMapping,Long>{
    @Query(value = "SELECT * FROM advertising_geofence_mapping m WHERE m.geo_id IN :geoId",nativeQuery = true)
    List<AdvertisementGeoFenceMapping> findByGeoFenceIds(@Param("geoId") List<Long> geoId);
}
