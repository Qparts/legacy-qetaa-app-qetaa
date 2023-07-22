package qetaa.jsf.beans.customer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import qetaa.jsf.beans.reqs.Requester;
import qetaa.jsf.helpers.AppConstants;
import qetaa.jsf.helpers.Bundler;
import qetaa.jsf.helpers.Helper;
import qetaa.jsf.model.customer.CustomerAccess;

@Named(value="resetPasswordBean")
@SessionScoped
public class ResetPasswordBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String emailMobile;
	private String systemSMS;
	private String userSMS;
	private String newPassword;
	private String confirmNewPassword;

	@Inject
	private Requester reqs;

	@Inject
	private LoginBean loginBean;

	public void resetPassword() {
		if (newPassword.equals(confirmNewPassword)) {
			if (userSMS.equals(this.systemSMS)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("mobile", emailMobile);
				map.put("password", newPassword);
				map.put("appSecret", AppConstants.APP_SECRET);
				Response r = reqs.putSecuredRequest(AppConstants.PUT_RESET_PASSWORD, map);
				if (r.getStatus() == 200) {
					loginBean.setAccess(r.readEntity(CustomerAccess.class));
					loginBean.setLoginStatus('A');
					loginBean.doLogin();
					Helper.redirect("/index");
				}
			}else {
				Helper.addErrorMessage(Bundler.getValue("invalidSms"));
			}
		} else {
			Helper.addErrorMessage(Bundler.getValue("passwordNotMatched"));
		}
	}

	public void requestSMS() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("mobile", this.emailMobile);
		Response r = reqs.postSecuredRequest(AppConstants.POST_RESET_SMS, map);
		if (r.getStatus() == 200) {
			this.systemSMS = r.readEntity(String.class);
		} else if (r.getStatus() == 409) {
			Helper.addErrorMessage(Bundler.getValue("customerNotFound"));
		} else {
			Helper.addErrorMessage(Bundler.getValue("errorOccured"));
		}
	}

	public String getEmailMobile() {
		return emailMobile;
	}

	public void setEmailMobile(String emailMobile) {
		this.emailMobile = emailMobile;
	}

	public String getSystemSMS() {
		return systemSMS;
	}

	public void setSystemSMS(String systemSMS) {
		this.systemSMS = systemSMS;
	}

	public String getUserSMS() {
		return userSMS;
	}

	public void setUserSMS(String userSMS) {
		this.userSMS = userSMS;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}

}
