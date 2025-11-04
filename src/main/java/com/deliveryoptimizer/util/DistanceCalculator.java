package com.deliveryoptimizer.util;

public class DistanceCalculator {

    private final double lant1;
    private final double lat1;
    private final double lant2;
     private final double lat2;

     private  static  double EARTH_RADIUS_KM = 6371.0;

    public DistanceCalculator(double lant1, double lat1, double lant2, double lat2) {
         this.lant1 = lant1;
         this.lat1 = lat1;
         this.lant2 = lant2;
         this.lat2 = lat2;
     }

     public static double DistanceCalc(double lant1, double lat1, double lant2, double lat2) {

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon =Math.toRadians(lant2 - lant1);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                *Math.sin(dLon/2)*Math.sin(dLon/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return EARTH_RADIUS_KM * c;
     }
}
