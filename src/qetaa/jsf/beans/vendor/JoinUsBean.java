package qetaa.jsf.beans.vendor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import qetaa.jsf.beans.customer.LoginBean;
import qetaa.jsf.beans.reqs.NotLoggedRequester;
import qetaa.jsf.helpers.AppConstants;
import qetaa.jsf.model.location.City;
import qetaa.jsf.model.location.Country;
import qetaa.jsf.model.location.Region;
import qetaa.jsf.model.vendor.VendorJoinRequest;

@Named
@ViewScoped
public class JoinUsBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private VendorJoinRequest joinUs;
	private List<Country> countries;
	private int selectedCountryId;
	private int selectedRegionId;
	private boolean submitted;
	
	@Inject
	private LoginBean loginBean;
	
	@Inject
	private NotLoggedRequester reqs;
	
	@PostConstruct
	private void init() {
		countries = loginBean.getCountries();
		submitted = false;
		reset();
				
	}
	
	private void reset() {
		joinUs = new VendorJoinRequest();
		selectedCountryId = 0;
		selectedRegionId = 0;
	}
	
	public void addNewVendor() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_JOIN_US, this.joinUs);
		System.out.println(r.getStatus());
		reset();
		submitted = true;
		
	}

	public VendorJoinRequest getJoinUs() {
		return joinUs;
	}
	
	public List<Region> getSelectedCountryRegions(){
		for(Country c : countries) {
			if(this.selectedCountryId == c.getId())
				return c.getRegions();
		}
		return new ArrayList<>();
	}
	
	public List<City> getSelectedRegionCities(){
		for(Country c : countries) {
			if(this.selectedCountryId == c.getId()) {
				for(Region r : c.getRegions()) {
					if(r.getId() == this.selectedRegionId) {
						return r.getCities();
					}
				}
			}
				
		}
		return new ArrayList<>();
	}

	public void setJoinUs(VendorJoinRequest joinUs) {
		this.joinUs = joinUs;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	public int getSelectedCountryId() {
		return selectedCountryId;
	}

	public void setSelectedCountryId(int selectedCountryId) {
		this.selectedCountryId = selectedCountryId;
	}

	public int getSelectedRegionId() {
		return selectedRegionId;
	}

	public void setSelectedRegionId(int selectedRegionId) {
		this.selectedRegionId = selectedRegionId;
	}

	public boolean isSubmitted() {
		return submitted;
	}

	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}

	
	

	
	
}
