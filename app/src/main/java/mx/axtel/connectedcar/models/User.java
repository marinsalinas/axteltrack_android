package mx.axtel.connectedcar.models;

import com.google.gson.annotations.Expose;

/**
 * Created by marinsalinas on 3/23/15.
 */
public class User {

    @Expose
    private String account;
    @Expose
    private String user;
    @Expose
    private String password;
    @Expose
    private String contactName;
    @Expose
    private String contactPhone;
    @Expose
    private String contactEmail;
    @Expose
    private String lastLoginTime;
    @Expose
    private String description;
    @Expose
    private String token;
    /**
     *
     * @return
     * The account
     */
    public String getAccount() {
        return account;
    }

    /**
     *
     * @param account
     * The account
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     *
     * @return
     * The user
     */
    public String getUser() {
        return user;
    }

    /**
     *
     * @param user
     * The user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     *
     * @return
     * The password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     * The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     * The contactName
     */
    public String getContactName() {
        return contactName;
    }

    /**
     *
     * @param contactName
     * The contactName
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     *
     * @return
     * The contactPhone
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     *
     * @param contactPhone
     * The contactPhone
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    /**
     *
     * @return
     * The contactEmail
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     *
     * @param contactEmail
     * The contactEmail
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     *
     * @return
     * The lastLoginTime
     */
    public String getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     *
     * @param lastLoginTime
     * The lastLoginTime
     */
    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
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
     * The token
     */
    public String getToken() {
        return token;
    }

    /**
     *
     * @param token
     * The token
     */
    public void setToken(String token) {
        this.token = token;
    }
}
