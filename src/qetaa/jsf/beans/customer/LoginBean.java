package qetaa.jsf.beans.customer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.primefaces.context.RequestContext;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.User;

import qetaa.jsf.beans.reqs.Requester;
import qetaa.jsf.helpers.AppConstants;
import qetaa.jsf.helpers.Bundler;
import qetaa.jsf.helpers.Helper;
import qetaa.jsf.helpers.PojoRequester;
import qetaa.jsf.model.customer.Customer;
import qetaa.jsf.model.customer.CustomerAccess;
import qetaa.jsf.model.customer.EmailAccess;
import qetaa.jsf.model.customer.FacebookAccess;
import qetaa.jsf.model.location.Country;

@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private char loginMechanism = 'N';
	private char loginStatus = 'N';

	private CustomerAccess access;
	private FacebookAccess facebookAccess;
	private EmailAccess emailAccess;
	private boolean remember;
	private boolean fromCookie;
	private String smsCode;
	private String smsCodeUser;
	private List<Country> countries;

	@Inject
	private Requester reqs;

	@Inject
	private AutoCartBean autoCartBean;

	@Inject ActivityMonitorBean monitorBean;
	@PostConstruct
	private void init() {
		remember = false;
		fromCookie = false;
		access = new CustomerAccess();
		access.setCustomer(new Customer());
		emailAccess = new EmailAccess();
		emailAccess.setAppSecret(AppConstants.APP_SECRET);
		
		// scheckCookieMap();
		initCountries();
	}

	public String getCountryRegex() {
		String regex = "";
		if (countries != null && emailAccess != null && null != emailAccess.getCountryId()) {
			for (Country c : countries) {
				if (c.getId() == this.emailAccess.getCountryId()) {
					regex = c.getMobileRegex();
					break;
				}
			}
		}
		return regex;

	}
	
	public String getFBCountryRegex() {
		String regex = "";
		if (countries != null && facebookAccess != null && null != facebookAccess.getCountryId()) {
			for (Country c : countries) {
				if (c.getId() == this.facebookAccess.getCountryId()) {
					regex = c.getMobileRegex();
					break;
				}
			}
		}
		return regex;

	}

	private void initCountries() {
		this.emailAccess.setCountryId(1);
		Response r = PojoRequester.getSecuredRequest(AppConstants.GET_ACTIVE_COUNTRIES, this.getSecurityHeader());
		if (r.getStatus() == 200) {
			countries = r.readEntity(new GenericType<List<Country>>() {
			});
		} else {
			countries = new ArrayList<>();
		}
	}

	private void scheckCookieMap() {
		String email = null;
		String password = null;
		Map<String, Object> requestCookieMap = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestCookieMap();
		for (Map.Entry<String, Object> entry : requestCookieMap.entrySet()) {
			Cookie c = (javax.servlet.http.Cookie) entry.getValue();
			if (c.getName().equals("email")) {
				email = c.getValue();
			} else if (c.getName().equals("password")) {
				password = c.getValue();
			}
		}
		if (email != null && password != null) {
			this.emailAccess.setPassword(password);
			this.emailAccess.setEmail(email);
			this.fromCookie = true;
			doEmailLogin();
			// do login from cookies
		}
	}

	private void saveCookie() {
		if (remember) {
			// create cookies
			Cookie emailCk = new Cookie("email", this.emailAccess.getEmail());
			Cookie passCk = new Cookie("password", this.emailAccess.getPassword());
			// set expiry date
			emailCk.setMaxAge(60 * 60 * 24);
			passCk.setMaxAge(60 * 60 * 24);
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			response.addCookie(emailCk);
			response.addCookie(passCk);
		}
	}

	public void chooseFacebookLogin() {
		loginMechanism = 'F';
		monitorBean.addToActivity("requested login/register from facebook");
		Helper.redirect(AppConstants.FB_DIALOG_URL);
	}

	public void checkLogin() {
		if (loginMechanism == 'F') {
			doFacebookLogin();
		} else if (loginMechanism == 'E') {
			Helper.redirect("/index");
		} else if (loginMechanism == 'N') {
			Helper.redirect("/index");
		}
	}

	public void checkCodeLogin() {
		monitorBean.addToActivity("login from code");
		String code = Helper.getParam("c");
		Response r = reqs.getSecuredRequest(AppConstants.getCodeLogin(code));
		if (r.getStatus() == 200) {
			monitorBean.addToActivity("successful code");
			this.access = r.readEntity(CustomerAccess.class);
			this.loginStatus = 'A';
			saveCookie();
			doLogin();
			Helper.redirect("/my_orders");
		} else {
			monitorBean.addToActivity("invalid code login");
			Helper.redirect("/index");
		}

	}

	public void chooseEmailRegistration() {
		monitorBean.addToActivity("chose email registration");
		this.loginMechanism = 'E';// registration
		this.loginStatus = 'R';
	}

	public void requestSMS() {
		monitorBean.addToActivity("request email register");
		if (this.emailAccess.getPassword().equals(this.emailAccess.getConfirmPassword())) {
			monitorBean.addToActivity("registration mobile: " + emailAccess.getMobile());
			monitorBean.addToActivity("registration email: "+ emailAccess.getEmail());
			monitorBean.addToActivity("registration name: " + emailAccess.getFirstName() + " " + emailAccess.getLastName());
			Map<String, String> map = new HashMap<String, String>();
			map.put("mobile", emailAccess.getMobile());
			map.put("email", emailAccess.getEmail());
			String link = "";
			if (this.emailAccess.getCountryId() == 1) {
				monitorBean.addToActivity("sending sms");
				map.put("countryCode", "966");
				emailAccess.setCountryCode("966");
				link = AppConstants.POST_REGISTER_SMS;
			} else {
				for (Country c : countries) {
					if (c.getId() == emailAccess.getCountryId()) {
						monitorBean.addToActivity("chose country: " + c.getName());
						map.put("countryCode", c.getCountryCode());
						emailAccess.setCountryCode(c.getCountryCode());
						break;
					}
				}
				link = AppConstants.POST_REGISTER_EMAIL;
			}
			Response r = reqs.postSecuredRequest(link, map);
			if (r.getStatus() == 200) {
				this.smsCode = r.readEntity(String.class);
				monitorBean.addToActivity("sms sent to customer: " + smsCode);
			} else if (r.getStatus() == 409) {
				monitorBean.addToActivity("customer already registered, failed registration");
				Helper.addErrorMessage(Bundler.getValue("customerRegistered"));
			} else {
				Helper.addErrorMessage(Bundler.getValue("errorOccured"));
			}
		} else {
			monitorBean.addToActivity("entered password did not match");
			Helper.addErrorMessage(Bundler.getValue("passwordNotMatched"));
		}
	}

	public void requestSMSFacebook() {
		monitorBean.addToActivity("request facebook register");
		Map<String, String> map = new HashMap<String, String>();
		monitorBean.addToActivity("facebook registration mobile: " + facebookAccess.getMobile());
		monitorBean.addToActivity("facebook registration email: "+ facebookAccess.getEmail());
		monitorBean.addToActivity("facebook registration name: " + facebookAccess.getFirstName() + " " + facebookAccess.getLastName());
		map.put("mobile", facebookAccess.getMobile());
		map.put("email", facebookAccess.getEmail());
		String link = "";
		if (this.facebookAccess.getCountryId() == 1) {
			monitorBean.addToActivity("sending sms");
			map.put("countryCode", "966");
			facebookAccess.setCountryCode("966");
			link = AppConstants.POST_REGISTER_SMS;
		}

		else {
			for (Country c : countries) {
				if (c.getId() == facebookAccess.getCountryId()) {
					monitorBean.addToActivity("chose country: " + c.getName());
					map.put("countryCode", c.getCountryCode());
					facebookAccess.setCountryCode(c.getCountryCode());
					break;
				}
			}
			link = AppConstants.POST_REGISTER_EMAIL;
		}

		Response r = reqs.postSecuredRequest(link, map);
		if (r.getStatus() == 200) {
			this.smsCode = r.readEntity(String.class);
			monitorBean.addToActivity("sms sent to customer: " + smsCode);
		} else if (r.getStatus() == 409) {
			monitorBean.addToActivity("customer already registered, failed registration");
			Helper.addErrorMessage(Bundler.getValue("customerRegistered"));
		} else {
			Helper.addErrorMessage(Bundler.getValue("errorOccured"));
		}
	}

	public void activateAndRegisterSMS() {
		monitorBean.addToActivity("verifying sms");
		if (this.smsCode.equals(this.smsCodeUser)) {
			emailAccess.setCreatedBy(0);
			String link = "";
			if (emailAccess.getCountryId() == 1) {
				monitorBean.addToActivity("register via mobile");
				link = AppConstants.POST_MOBILE_REGISTER;
			} else {
				monitorBean.addToActivity("register via email");
				link = AppConstants.POST_EMAIL_REGISTER;
			}
			Response r = reqs.postSecuredRequest(link, emailAccess);
			if (r.getStatus() == 200) {
				monitorBean.addToActivity("successful registration mobile: " + emailAccess.getMobile());
				this.access = r.readEntity(CustomerAccess.class);
				this.loginStatus = 'A';
				this.emailAccess = new EmailAccess();
				doLogin();
			} else {
				monitorBean.addToActivity("failed registration");
			}
			// create and activate
		} else {
			monitorBean.addToActivity("entered invalid sms code");
			Helper.addErrorMessage(Bundler.getValue("invalidSms"));
		}
	}

	public void activateAndRegisterSMSFaceook() {
		monitorBean.addToActivity("verifying sms");
		if (this.smsCode.equals(this.smsCodeUser)) {
			Response r = reqs.postSecuredRequest(AppConstants.POST_FACEBOOK_REGISTER, facebookAccess);
			if (r.getStatus() == 200) {
				monitorBean.addToActivity("successful registration mobile: " + facebookAccess.getMobile());
				this.access = r.readEntity(CustomerAccess.class);
				this.loginStatus = 'A';
				this.facebookAccess = new FacebookAccess();
				doLogin();
			}
		} else {
			monitorBean.addToActivity("entered invalid sms");
			Helper.addErrorMessage(Bundler.getValue("invalidSms"));
		}
	}

	public void doEmailLogin() {
		monitorBean.addToActivity("tried to login as " + emailAccess.getEmail());
		// only encrypt if it is not from cookie
		if (!fromCookie) {
			emailAccess.setPassword(Helper.cypherSha256(emailAccess.getPassword()));
		}
		Response r = reqs.postSecuredRequest(AppConstants.POST_EMAIL_LOGIN, emailAccess, null, 0);
		if (r.getStatus() == 201) {			
			this.access = r.readEntity(CustomerAccess.class);
			this.loginStatus = 'V';// needs verification
			doLogin();
		} else if (r.getStatus() == 404) {
			monitorBean.addToActivity("wrong password in login");
			Helper.addErrorMessage(Bundler.getValue("passwordNotMatched"));
		} else if (r.getStatus() == 500) {
			Helper.addErrorMessage(Bundler.getValue("errorOccured"));
		} else if (r.getStatus() == 200) {
			this.access = r.readEntity(CustomerAccess.class);
			this.loginStatus = 'A';
			saveCookie();
			doLogin();
		}
	}

	private void deleteCookie() {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("maxAge", 0);
		/*
		 * the cookie I want to overwrite (expire)
		 */
		FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("email", "", props);
		/*
		 * the cookie I want to overwrite (expire)
		 */
		FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("password", "", props); // t
	}

	private void doFacebookLogin() {
		String code = Helper.getParam("code");
		if (code != null) {
			monitorBean.addToActivity("received login code from facebook");
			FacebookClient client = new DefaultFacebookClient(Version.VERSION_2_8);
			AccessToken token = client.obtainUserAccessToken(AppConstants.FB_APP_ID, AppConstants.FB_APP_SECRET,
					AppConstants.PAGE_SUCCESSFUL, code);
			client = new DefaultFacebookClient(token.getAccessToken(), Version.VERSION_2_8);
			User user = client.fetchObject("me", User.class, Parameter.with("fields", "id,first_name,last_name,email"));
			facebookAccess = new FacebookAccess(user, null);
			facebookAccess.setCountryId(1);
			Response r = reqs.postSecuredRequest(AppConstants.POST_FACEBOOK_LOGIN, facebookAccess);
			if (r.getStatus() == 404) {
				monitorBean.addToActivity("requested login from customer but not registered, redirected to register dialog");
				this.loginStatus = 'R';// needs registration
				Helper.redirect("/index");

			} else if (r.getStatus() == 201) {
				monitorBean.addToActivity("successful login from facebook");
				this.access = r.readEntity(CustomerAccess.class);
				this.loginStatus = 'V';// needs verification
				doLogin();

			} else if (r.getStatus() == 200) {
				monitorBean.addToActivity("successful login from facebook");
				this.loginStatus = 'A';
				this.access = r.readEntity(CustomerAccess.class);
				Helper.redirect("/index");
				// login
				doLogin();
			}
		} else {
			monitorBean.addToActivity("error in facebook code");
		}
	}

	public String getSecurityHeader() {
		return "Bearer " + this.getAccess().getToken() + " && " + this.getAccess().getCustomer().getId() + " && "
				+ AppConstants.APP_SECRET + " && C";// from user
	}

	public void updateLogin(CustomerAccess access) {
		this.access = access;
		doLogin();
	}

	public void doLogin() {
		monitorBean.addToActivity("successful login");
		monitorBean.initCustomer();
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("customer.access", access);
		if (autoCartBean.getStep() == 5) {
			RequestContext.getCurrentInstance().execute("resetActive(" + autoCartBean.getProgressPercentage() + ", '"
					+ autoCartBean.getProgressStepName() + "');");
			RequestContext.getCurrentInstance().execute("showCartDialog()");
		}
	}

	public void doLogout() {
		monitorBean.addToActivity("logged out");
		deleteCookie();
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("customer.access");
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		Helper.redirect("/index");
	}

	public boolean isLogged() {
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customer.access") != null;
	}

	public char getLoginMechanism() {
		return loginMechanism;
	}

	public char getLoginStatus() {
		return loginStatus;
	}

	public CustomerAccess getAccess() {
		return access;
	}

	public FacebookAccess getFacebookAccess() {
		return facebookAccess;
	}

	public EmailAccess getEmailAccess() {
		return emailAccess;
	}

	public boolean isRemember() {
		return remember;
	}

	public void setRemember(boolean remember) {
		this.remember = remember;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getSmsCodeUser() {
		return smsCodeUser;
	}

	public void setSmsCodeUser(String smsCodeUser) {
		this.smsCodeUser = smsCodeUser;
	}

	public void setAccess(CustomerAccess access) {
		this.access = access;
	}

	public void setLoginStatus(char loginStatus) {
		this.loginStatus = loginStatus;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

}
