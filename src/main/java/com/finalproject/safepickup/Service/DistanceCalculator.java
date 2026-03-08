package com.finalproject.safepickup.Service;

public class DistanceCalculator {

    private static final int EARTH_RADIUS = 6371; // Radius in kilometers

    /*
     * Calculate distance between two points using Haversine formula
     *  return: distance in meters
     */
    public static int calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distanceKm = EARTH_RADIUS * c;
        return (int) (distanceKm * 1000); // Convert to meters
    }
}
