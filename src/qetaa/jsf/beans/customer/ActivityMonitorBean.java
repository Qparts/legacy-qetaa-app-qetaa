package qetaa.jsf.beans.customer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import qetaa.jsf.beans.reqs.Requester;
import qetaa.jsf.helpers.AppConstants;
import qetaa.jsf.helpers.PojoRequester;
import qetaa.jsf.model.customer.Customer;
import qetaa.jsf.model.customer.HitActivity;

@Named(value = "activityMonitorBean")
@SessionScoped
public class ActivityMonitorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<HitActivity> activities;

	@Inject
	private Requester reqs;

	private String os;
	private String ip;
	private String sessionId;
	private String header;
	private Long visitIndex;

	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		activities = new ArrayList<>();
		header = reqs.getSecurityHeader();
		this.initOS();
		this.initIp();
		this.initSessionId();
		this.initVisitIndex();
	}
	
	private void initVisitIndex() {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> map = context.getRequestCookieMap();
		Object vio = map.get("visitIndex");
		if(null == vio) {
			Response r = PojoRequester.getSecuredRequest(AppConstants.GET_NEW_VISIT_INDEX, header);
			if (r.getStatus() == 200) {
				this.visitIndex = r.readEntity(Long.class);
				System.out.println("new visit index " + visitIndex);
				saveVisitIndexCookie();
			} else {
				System.out.println("could not get visit index");
			}
		}
		else {
			Cookie cookie = (Cookie) vio;
			this.visitIndex = Long.valueOf((String)cookie.getValue());
			System.out.println("retreived cookie index : " + visitIndex);
		}
	}
	
	private void saveVisitIndexCookie() {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		Cookie cookie = new Cookie("visitIndex", visitIndex.toString());
		cookie.setMaxAge(60* 60 * 24 * 365 * 10);
		
		 HttpServletResponse response = (HttpServletResponse) context.getResponse();
		 response.addCookie(cookie);
	}

	private void initSessionId() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		sessionId = session.getId();
	}

	private void initIp() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		ip = request.getHeader("X-FORWARDED-FOR");
		if (ip != null) {
			// cares only about the first IP if there is a list
			ip = ip.replaceFirst(",.*", "");
		} else {
			ip = request.getRemoteAddr();
		}
	}

	private void initOS() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String userAgent = request.getHeader("user-agent");
		// =============OS
		if (userAgent.toLowerCase().indexOf("windows") >= 0) {
			os = "Windows";
		} else if (userAgent.toLowerCase().indexOf("mac") >= 0) {
			os = "Mac";
		} else if (userAgent.toLowerCase().indexOf("x11") >= 0) {
			os = "Unix";
		} else if (userAgent.toLowerCase().indexOf("android") >= 0) {
			os = "Android";
		} else if (userAgent.toLowerCase().indexOf("iphone") >= 0 || userAgent.toLowerCase().indexOf("ipad") >= 0) {
			os = "iOS";
		} else {
			os = "UnKnown";
		}
	}

	public void addToActivity(String desc) {
		HitActivity hit = new HitActivity();
		hit.setActivityDes(desc);
		hit.setCreated(new Date());
		hit.setDevice(os);
		hit.setIp(ip);
		hit.setVisitIndex(visitIndex);
		hit.setSessionId(sessionId);
		this.activities.add(hit);
	}

	public void initCustomer() {
		if (null != loginBean.getAccess().getCustomer()) {
			header = reqs.getSecurityHeader();
			for (HitActivity hit : activities) {
				hit.setCustomer(loginBean.getAccess().getCustomer());
			}
		}
	}

	private void preDestroyInitCustomer() {
		Customer customer = null;
		for (HitActivity hi : activities) {
			if (null != hi.getCustomer()) {
				customer = hi.getCustomer();
				break;
			}
		}
		if (null != customer) {
			for (HitActivity hi : activities) {
				hi.setCustomer(customer);
			}
		}
	}

	@PreDestroy
	private void destroy() {
		preDestroyInitCustomer();
		if (activities.size() > 1) {
			Response r = PojoRequester.postSecuredRequest(AppConstants.POST_HIT_ACTIVITIES, activities, header);
			if (r.getStatus() == 201) {
				
			} else {
				System.out.println("could not post activites");
			}
		}
	}

	public List<HitActivity> getActivities() {
		return activities;
	}

	public void setActivities(List<HitActivity> activities) {
		this.activities = activities;
	}

}
