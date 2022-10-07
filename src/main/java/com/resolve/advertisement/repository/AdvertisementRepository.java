package com.resolve.advertisement.repository;

import com.resolve.advertisement.entity.AdvertisementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisementRepository extends JpaRepository<AdvertisementEntity,Long> {
}
