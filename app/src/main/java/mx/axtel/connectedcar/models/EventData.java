package mx.axtel.connectedcar.models;
import com.google.gson.annotations.Expose;

import java.util.Date;


public class EventData {

    @Expose
    private String accountID;
    @Expose
    private String deviceID;
    @Expose
    private Date timestamp;
    @Expose
    private String statusCode;
    @Expose
    private double latitude;
    @Expose
    private double longitude;
    @Expose
    private double speedKPH;
    @Expose
    private double heading;

    /**
     *
     * @return
     * The accountID
     */
    public String getAccountID() {
        return accountID;
    }

    /**
     *
     * @param accountID
     * The accountID
     */
    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    /**
     *
     * @return
     * The deviceID
     */
    public String getDeviceID() {
        return deviceID;
    }

    /**
     *
     * @param deviceID
     * The deviceID
     */
    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    /**
     *
     * @return
     * The timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     *
     * @param timestamp
     * The timestamp
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     *
     * @return
     * The statusCode
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     *
     * @param statusCode
     * The statusCode
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     *
     * @return
     * The latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     * The latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     * The longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     * The longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @return
     * The speedKPH
     */
    public double getSpeedKPH() {
        return speedKPH;
    }

    /**
     *
     * @param speedKPH
     * The speedKPH
     */
    public void setSpeedKPH(double speedKPH) {
        this.speedKPH = speedKPH;
    }

    /**
     *
     * @return
     * The heading
     */
    public double getHeading() {
        return heading;
    }

    /**
     *
     * @param heading
     * The heading
     */
    public void setHeading(double heading) {
        this.heading = heading;
    }


}