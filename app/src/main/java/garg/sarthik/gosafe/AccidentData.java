package garg.sarthik.gosafe;

public class AccidentData {

    String state;
    String city;
    String Locality;
    double latitude;
    double longitude;
    int percentage;

    public AccidentData(String state, String city, String locality, double latitude, double longitude, int percentage) {
        this.state = state;
        this.city = city;
        Locality = locality;
        this.latitude = latitude;
        this.longitude = longitude;
        this.percentage = percentage;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getPercentage() {
        return percentage;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getLocality() {
        return Locality;
    }
}
