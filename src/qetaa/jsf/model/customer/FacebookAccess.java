package qetaa.jsf.model.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restfb.types.User;

import qetaa.jsf.helpers.AppConstants;

public class FacebookAccess {
	private String appSecret;//from which app was logged in
	private String email;//email from facebook
	private String contactEmail;//different email if not facebook email
	private long facebookId;//facebook id
	private String firstName;//first name from facebook
	private String lastName;//last name from facebook
	private String mobile;//mobile number (only for registration)
	private Integer countryId;
	private String countryCode;
	
	public FacebookAccess() {
		
	}
	@JsonIgnore
	public FacebookAccess(User user, String mobile){
		setAppSecret(AppConstants.APP_SECRET);
		setEmail(user.getEmail());
		setFacebookId(Long.parseLong(user.getId()));
		setContactEmail(user.getEmail());
		setFirstName(user.getFirstName());
		setLastName(user.getLastName());
		setMobile(mobile);
	}
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public String getEmail() {
		return email;
	}
	public long getFacebookId() {
		return facebookId;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setFacebookId(long facebookId) {
		this.facebookId = facebookId;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	

}
