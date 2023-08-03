package qetaa.jsf.beans.customer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.primefaces.PrimeFaces;

import qetaa.jsf.beans.reqs.NotLoggedRequester;
import qetaa.jsf.beans.reqs.Requester;
import qetaa.jsf.helpers.AppConstants;
import qetaa.jsf.helpers.Bundler;
import qetaa.jsf.helpers.Helper;
import qetaa.jsf.helpers.PojoRequester;
import qetaa.jsf.model.cart.Cart;
import qetaa.jsf.model.cart.CartItem;
import qetaa.jsf.model.location.Country;
import qetaa.jsf.model.location.Region;
import qetaa.jsf.model.vehicle.Make;
import qetaa.jsf.model.vehicle.Model;
import qetaa.jsf.model.vehicle.ModelYear;

@Named(value = "autocartbean")
@SessionScoped
public class AutoCartBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Cart cart;
	private List<Make> makes;
	private int step;
	private Make selectedMake;
	private Model selectedModel;
	private ModelYear selectedModelYear;
	private int yearNumber;
	private int[] quantityArray;
	private Long lastOrderId;
	private boolean lastNoVin;
	private boolean vinImageUploaded;
	private boolean lastVinImage;
	private List<Country> countries;
	private Region selectedRegion;
	private String os;
	private String ip;
	private boolean agree;
	private boolean promCode;
	private boolean promVerified;
	private String promCodeString;
	private int selectedRegionId;
	private String vinBase64;

	@Inject
	private NotLoggedRequester reqsNotLogged;
	@Inject
	private ActivityMonitorBean monitorBean;
	@Inject
	private Requester reqs;

	@Inject
	private LoginBean loginBean;

	@Inject
	private MyOrdersBean myOrdersBean;

	@PostConstruct
	private void beanInit() {
		vinImageUploaded = false;
		vinBase64 = "";
		initMakes();
		initCountries();
		init();
		// in different thread
		initOS();
		initIp();
		addMetas();
	}

	private void addMetas() {
		String header = reqs.getSecurityHeader();
		addVisitCounter(header);
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

	private void addVisitCounter(String header) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ip", ip);
		map.put("device", os);
		Response r = PojoRequester.postSecuredRequest(AppConstants.POST_HIT_COUNTER, map, header);
		if (r.getStatus() == 201) {

		} else {

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

	private void init() {
		vinImageUploaded = false;
		vinBase64 = "";
		cart = new Cart();
		cart.setCartItems(new ArrayList<>());
		step = 0;
		initQuantityArray();
		addItem();
		this.selectedMake = null;
		this.selectedModel = null;
		this.promCode = false;
		this.promCodeString = "";
		this.promVerified = false;
	}

	public String getFullVehicleName() {
		String s = "";
		switch (step) {
		case 1:
			s = s + this.selectedMake.getNameAr();
			break;
		case 2:
			s = s + this.selectedMake.getNameAr() + " - " + this.selectedModel.getNameAr();
			break;
		case 3:
		case 4:
		case 5:
			s = s + this.selectedMake.getNameAr() + " - " + this.selectedModel.getNameAr() + " - " + this.yearNumber;
		}
		return s;
	}

	public void addItem() {
		CartItem item = new CartItem();
		cart.getCartItems().add(item);
	}

	public void addItem(String desc) {
		monitorBean.addToActivity(desc);
		addItem();
	}

	public void removeItem(CartItem item) {
		monitorBean.addToActivity("removed item: " + item.getName() + ", quantity: " + item.getQuantity());
		this.cart.getCartItems().remove(item);
	}

	private void initQuantityArray() {
		quantityArray = new int[20];
		for (int i = 0; i < quantityArray.length; i++) {
			quantityArray[i] = i + 1;
		}
	}

	private void initMakes() {
		makes = new ArrayList<>();
		Response r = reqsNotLogged.getSecuredRequest(AppConstants.GET_ACTIVE_MAKES);
		if (r.getStatus() == 200) {
			makes = r.readEntity(new GenericType<List<Make>>() {
			});
		} else {

		}
	}
	
	public boolean isPromCodeVerifiedAll() {
		if(this.promCode) {
			return this.promVerified;
		}
		else return true;
	}
	
	
	public void verifyPromCode() {
		Response r = reqs.getSecuredRequest(AppConstants.getPromotionCodeFromCode(this.promCodeString));
		if(r.getStatus() == 200) {
			this.promVerified = true;
			this.cart.setPromotionCode(r.readEntity(Integer.class));
		}
		else if(r.getStatus() == 498) {
			Helper.addErrorMessage(Bundler.getValue("promCodeQuestion"));
		}
		else if(r.getStatus() == 410) {
			Helper.addErrorMessage(Bundler.getValue("promCodeUsed"));
		}
		else if (r.getStatus() == 404) {
			Helper.addErrorMessage(Bundler.getValue("promCodeNotFound"));
		}
		else {
			Helper.addErrorMessage(Bundler.getValue("errorOccured"));
		}
	}

	private void initCountries() {
		Response r = reqsNotLogged.getSecuredRequest(AppConstants.GET_ACTIVE_COUNTRIES);
		if (r.getStatus() == 200) {
			countries = r.readEntity(new GenericType<List<Country>>() {
			});
		} else {
			countries = new ArrayList<>();
		}
		// get saudi arabia cities only
	}
	
	

	public void submit() {
		monitorBean.addToActivity("chose city: " + cart.getCityId());
		monitorBean.addToActivity("submitting order");
		cart.setCustomerId(this.loginBean.getAccess().getCustomer().getId());
		cart.setCreatedBy(0);
		cart.setMakeId(this.selectedMake.getId());
		cart.setVatPercentage(0.05);
		if(this.vinImageUploaded) {
			cart.setNoVin(true);
			cart.setVinImage(true);
		}
		for (CartItem ci : cart.getCartItems()) {
			ci.setCreatedBy(0);
			ci.setName(ci.getName().trim());
		}
		Response r = reqs.postSecuredRequest(AppConstants.POST_CREATE_CART, cart);
		if (r.getStatus() == 200) {
			this.lastOrderId = r.readEntity(Long.class);
			PrimeFaces.current().executeScript("showCompleteDialog()");
			monitorBean.addToActivity("successful order submitted: " + lastOrderId);
			if(this.vinImageUploaded) {
				try {
					Helper.writeImage(this.vinBase64, lastOrderId, new Date());
					this.lastVinImage = true;
				}catch(Exception ex) {
					ex.printStackTrace();
					System.out.println("an error occured while saving image");
				}
			}
			this.lastNoVin = cart.isNoVin();
			init();
			
			PrimeFaces.current().executeScript("document.getElementById('form-1:vin-img').value = '';"); 		
			
		} else if (r.getStatus() == 429) {
			// log too many requests
		} else {
			// log exception
		}
	}

	public Country getCustomerCountry() {
		int countryId = loginBean.getAccess().getCustomer().getCountryId();
		for (Country c : countries) {
			if (c.getId() == countryId) {
				return c;
			}
		}
		Country c = new Country();
		c.setId(1);
		return c;
	}

	public void chooseRegion() {
		Country country = getCustomerCountry();
		if (this.selectedRegionId > 0) {
			for (Region region : country.getRegions()) {
				if (selectedRegionId == region.getId()) {
					this.selectedRegion = region;
					monitorBean.addToActivity(
							"chose region " + selectedRegion.getCountry().getName() + " - " + selectedRegion.getName());
					break;
				}
			}
		} else {
			this.cart.setCityId(0);
		}
	}

	public void verifyItems() {
		if (cart.getCartItems().size() == 0) {
			monitorBean.addToActivity("clicked next but no items added");
			PrimeFaces.current().executeScript("resetActive(80, 'step-5');");
			PrimeFaces.current().ajax().update("form-1:item-msg");
			Helper.addErrorMessage(Bundler.getValue("addItemsReq"));
		} else {
			for (CartItem citem : cart.getCartItems()) {
				monitorBean.addToActivity("added item: " + citem.getName() + ", quantity : " + citem.getQuantity());
			}
			PrimeFaces.current().ajax().update("form-1:panel");
			PrimeFaces.current().executeScript("resetActive(100, 'step-6');");
			this.step = 5;
		}
	}

	public int getProgressPercentage() {
		switch (step) {
		case 0:
			return 0;
		case 1:
			return 20;
		case 2:
			return 40;
		case 3:
			return 60;
		case 4:
			return 80;
		case 5:
			return 100;
		default:
			return 0;
		}
	}

	public String getProgressStepName() {
		switch (step) {
		case 0:
			return "step-1";
		case 1:
			return "step-2";
		case 2:
			return "step-3";
		case 3:
			return "step-4";
		case 4:
			return "step-5";
		case 5:
			return "step-6";
		default:
			return "step-1";
		}
	}

	public void clickOrderButton(String button, int i) {
		monitorBean.addToActivity("clicked " + button);
		resetToStep(i);
	}

	public void resetToStep(int i) {
		switch (i) {
		case 0:
			monitorBean.addToActivity("clicked back from models tab");
			this.step = 0;
			this.selectedMake = null;
			this.selectedModel = null;
			this.cart.setVehicleYear(null);
			PrimeFaces.current().executeScript("resetActive(0, 'step-1');");
			break;
		case 1:
			monitorBean.addToActivity("clicked back from model years tab");
			this.step = 1;
			this.selectedModel = null;
			this.cart.setVehicleYear(null);
			PrimeFaces.current().executeScript("resetActive(20, 'step-2');");
			break;
		case 2:
			monitorBean.addToActivity("clicked back from vin tab");
			this.step = 2;
			this.cart.setVehicleYear(null);
			PrimeFaces.current().executeScript("resetActive(40, 'step-3');");
			break;
		case 3:
			monitorBean.addToActivity("clicked back from add items tab");
			this.step = 3;
			PrimeFaces.current().executeScript("resetActive(60, 'step-4');");
			break;
		case 4:
			monitorBean.addToActivity("clicked back from submit tab");
			this.step = 4;
			PrimeFaces.current().executeScript("resetActive(80, 'step-5');");
			break;
		case 5:
			this.step = 5;
			PrimeFaces.current().executeScript("resetActive(100, 'step-6');");
			break;
		}
	}

	public void chooseMake(Make selectedMake) {
		monitorBean.addToActivity("chose make: " + selectedMake.getName());
		this.selectedMake = selectedMake;
		this.step = 1;
	}

	public void chooseModel(Model selectedType) {
		monitorBean.addToActivity("chose model: " + selectedType.getName());
		this.selectedModel = selectedType;
		this.step = 2;
	}

	public void chooseModelYear(ModelYear model) {
		monitorBean.addToActivity("chose model year: " + model.getYear());
		this.yearNumber = model.getYear();
		this.selectedModelYear = model;
		this.cart.setVehicleYear(model.getId());
		this.step = 3;
	}

	public void verifyVin() {
		if(this.vinBase64.length() > 0) {
			this.vinImageUploaded = true;
		}
		if (!this.cart.isNoVin() && !this.vinImageUploaded) {
			if (this.cart.getVin().length() == 17) {
				monitorBean.addToActivity("entered correct vin: " + cart.getVin());
				PrimeFaces.current().executeScript("resetActive(80, 'step-5');");
				this.step = 4;
				this.vinImageUploaded = false;
			} else {
				monitorBean.addToActivity("entered wrong vin: " + cart.getVin());
				PrimeFaces.current().executeScript("resetActive(60, 'step-4');");
				Helper.addErrorMessage(Bundler.getValue("invalidVin"), "form-1:vin");
			}
		}else {
			cart.setVin("");
			monitorBean.addToActivity("selected no vin ");
			PrimeFaces.current().executeScript("resetActive(80, 'step-5');");
			this.step = 4;
		}
	}
	
	public void resetVinImage() {
		monitorBean.addToActivity("deleted vin image");
		this.vinBase64 = "";
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public Make getSelectedMake() {
		return selectedMake;
	}

	public void setSelectedMake(Make selectedMake) {
		this.selectedMake = selectedMake;
	}

	public Model getSelectedModel() {
		return selectedModel;
	}

	public void setSelectedModel(Model selectedType) {
		this.selectedModel = selectedType;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public int[] getQuantityArray() {
		return quantityArray;
	}

	public void setQuantityArray(int[] quantityArray) {
		this.quantityArray = quantityArray;
	}

	public List<Make> getMakes() {
		return makes;
	}

	public Long getLastOrderId() {
		return lastOrderId;
	}

	public boolean isAgree() {
		return agree;
	}

	public void setAgree(boolean agree) {
		this.agree = agree;
	}

	public Region getSelectedRegion() {
		return selectedRegion;
	}

	public void setSelectedRegion(Region selectedRegion) {
		this.selectedRegion = selectedRegion;
	}

	public int getSelectedRegionId() {
		return selectedRegionId;
	}

	public void setSelectedRegionId(int selectedRegionId) {
		this.selectedRegionId = selectedRegionId;
	}

	public ModelYear getSelectedModelYear() {
		return selectedModelYear;
	}

	public void setSelectedModelYear(ModelYear selectedModelYear) {
		this.selectedModelYear = selectedModelYear;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	public boolean isLastNoVin() {
		return lastNoVin;
	}

	public void setLastNoVin(boolean lastNoVin) {
		this.lastNoVin = lastNoVin;
	}

	public String getVinBase64() {
		return vinBase64;
	}

	public void setVinBase64(String vinBase64) {
		this.vinBase64 = vinBase64;
	}

	public boolean isLastVinImage() {
		return lastVinImage;
	}

	public boolean isPromCode() {
		return promCode;
	}

	public void setPromCode(boolean promCode) {
		this.promCode = promCode;
	}

	public boolean isPromVerified() {
		return promVerified;
	}

	public void setPromVerified(boolean promVerified) {
		this.promVerified = promVerified;
	}

	public String getPromCodeString() {
		return promCodeString;
	}

	public void setPromCodeString(String promCodeString) {
		this.promCodeString = promCodeString;
	}
	
	
	

}
