package mx.axtel.connectedcar.models;

import com.google.gson.annotations.Expose;

/**
 * Created by marinsalinas on 4/1/15.
 */
public class Device {

    @Expose
    private String accountID;
    @Expose
    private String deviceID;
    @Expose
    private String groupID;
    @Expose
    private String uniqueID;
    @Expose
    private Object simPhoneNumber;
    @Expose
    private String lastValidLatitude;
    @Expose
    private String lastValidLongitude;
    @Expose
    private String lastValidHeading;
    @Expose
    private String lastGPSTimestamp;
    @Expose
    private String lastEventTimestamp;
    @Expose
    private String lastOdometerKM;
    @Expose
    private String isActive;
    @Expose
    private String displayName;
    @Expose
    private String description;
    @Expose
    private String lastUpdateTime;

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
     * The groupID
     */
    public String getGroupID() {
        return groupID;
    }

    /**
     *
     * @param groupID
     * The groupID
     */
    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    /**
     *
     * @return
     * The uniqueID
     */
    public String getUniqueID() {
        return uniqueID;
    }

    /**
     *
     * @param uniqueID
     * The uniqueID
     */
    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    /**
     *
     * @return
     * The simPhoneNumber
     */
    public Object getSimPhoneNumber() {
        return simPhoneNumber;
    }

    /**
     *
     * @param simPhoneNumber
     * The simPhoneNumber
     */
    public void setSimPhoneNumber(Object simPhoneNumber) {
        this.simPhoneNumber = simPhoneNumber;
    }

    /**
     *
     * @return
     * The lastValidLatitude
     */
    public String getLastValidLatitude() {
        return lastValidLatitude;
    }

    /**
     *
     * @param lastValidLatitude
     * The lastValidLatitude
     */
    public void setLastValidLatitude(String lastValidLatitude) {
        this.lastValidLatitude = lastValidLatitude;
    }

    /**
     *
     * @return
     * The lastValidLongitude
     */
    public String getLastValidLongitude() {
        return lastValidLongitude;
    }

    /**
     *
     * @param lastValidLongitude
     * The lastValidLongitude
     */
    public void setLastValidLongitude(String lastValidLongitude) {
        this.lastValidLongitude = lastValidLongitude;
    }

    /**
     *
     * @return
     * The lastValidHeading
     */
    public String getLastValidHeading() {
        return lastValidHeading;
    }

    /**
     *
     * @param lastValidHeading
     * The lastValidHeading
     */
    public void setLastValidHeading(String lastValidHeading) {
        this.lastValidHeading = lastValidHeading;
    }

    /**
     *
     * @return
     * The lastGPSTimestamp
     */
    public String getLastGPSTimestamp() {
        return lastGPSTimestamp;
    }

    /**
     *
     * @param lastGPSTimestamp
     * The lastGPSTimestamp
     */
    public void setLastGPSTimestamp(String lastGPSTimestamp) {
        this.lastGPSTimestamp = lastGPSTimestamp;
    }

    /**
     *
     * @return
     * The lastEventTimestamp
     */
    public String getLastEventTimestamp() {
        return lastEventTimestamp;
    }

    /**
     *
     * @param lastEventTimestamp
     * The lastEventTimestamp
     */
    public void setLastEventTimestamp(String lastEventTimestamp) {
        this.lastEventTimestamp = lastEventTimestamp;
    }

    /**
     *
     * @return
     * The lastOdometerKM
     */
    public String getLastOdometerKM() {
        return lastOdometerKM;
    }

    /**
     *
     * @param lastOdometerKM
     * The lastOdometerKM
     */
    public void setLastOdometerKM(String lastOdometerKM) {
        this.lastOdometerKM = lastOdometerKM;
    }

    /**
     *
     * @return
     * The isActive
     */
    public String getIsActive() {
        return isActive;
    }

    /**
     *
     * @param isActive
     * The isActive
     */
    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    /**
     *
     * @return
     * The displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     *
     * @param displayName
     * The displayName
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The lastUpdateTime
     */
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     *
     * @param lastUpdateTime
     * The lastUpdateTime
     */
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

}