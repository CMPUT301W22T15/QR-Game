package com.example.qrgameteam15;

/**
 *
 */
public class Location {
    private double latitude;
    private double longitude;
    private String countryName;
    private String cityName;
    private String address;

    /**
     *
     * @param latitude
     * @param longitude
     * @param countryName
     * @param cityName
     * @param address
     */
    public Location(double latitude, double longitude, String countryName, String cityName, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.countryName = countryName;
        this.cityName = cityName;
        this.address = address;
    }

    /**
     *
     * @return
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     *
     * @return
     */
    public double getLongitude() {
        return longitude;
    }

    public String getCountryName() {
        return countryName;
    }

    /**
     *
     * @return
     */
    public String getCityName() {
        return cityName;
    }

    /**
     *
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param latitude2
     * @param longitude2
     * @return
     */
    public double calculateDistanceKm(double latitude2, double longitude2) {
        double theta = this.longitude-longitude2;
        double distance = Math.sin(deg2rad(this.latitude)) * Math.sin(deg2rad(latitude2)) + Math.cos(deg2rad(this.latitude)) * Math.cos(deg2rad(latitude2)) * Math.cos(deg2rad(theta));
        distance = Math.acos(distance);
        distance = rad2deg(distance);
        distance = distance * 60 * 1.1515 * 1.609344;
        return distance; // returns the distance in kilometers
    }

    /**
     *
     * @param degree
     * @return
     */
    public double deg2rad(double degree) {
        return ((Math.PI/180.0)*degree);
    }

    /**
     *
     * @param radian
     * @return
     */
    public double rad2deg(double radian) {
        return ((180.0/Math.PI)*radian);
    }
}
