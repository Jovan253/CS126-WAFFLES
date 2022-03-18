package uk.ac.warwick.cs126.util;

public class HaversineDistanceCalculator {

    private final static float R = 6372.8f;
    private final static float kilometresInAMile = 1.609344f;

    public static float inKilometres(float lat1, float lon1, float lat2, float lon2) {

        double dLat = (lat2 - lat1) *Math.PI / 180;
        double dLon = (lon2 - lon1) *Math.PI / 180;

        // convert to radians
        double lati1 = (lat1) * Math.PI / 180;
        double lati2 = (lat2) * Math.PI / 180;

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lati1) * Math.cos(lati2);
        double c = 2 * Math.asin(Math.sqrt(a));
        float C = (float) c;
        float d =  (float) R*C;
        return d;
    }

    public static float inMiles(float lat1, float lon1, float lat2, float lon2) {
        // TODO
        return (float) Math.round((inKilometres(lat1, lon1, lat2, lon2)/kilometresInAMile)*10)/10;
    }
}