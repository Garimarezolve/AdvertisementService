package com.resolve.advertisement.utils;

import com.resolve.advertisement.dto.GeoFence;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.SloppyMath;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DistanceCalculator {

    /*
	 This function checks whether the given lat and long is inside the radius or not
	 */
    public boolean checkInsideGeoLocation(GeoFence circle, Double longitude, Double latitude) {
        return haversineDistance(
                circle.getLongitude(), circle.getLatitude(), longitude, latitude
        ) < circle.getRadius();
    }

    /*
    Below function is the formula to return the distance between two geolocation lat and long.
    This formula is based on harvestine distance calculator.
     */
    public double haversineDistance(Double lon1, Double lat1, Double lon2, Double lat2)
    {
        log.info("finding distance of {} {} , {} {}",lat1,lon1,lat2,lon2);
        double result =  SloppyMath.haversinMeters(lat1,lon1,lat2,lon2);
        log.info("Distance between two geo location is {}",result);
        return result;
    }
}
