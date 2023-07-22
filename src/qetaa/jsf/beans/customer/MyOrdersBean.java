package qetaa.jsf.beans.customer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.beans.reqs.Requester;
import qetaa.jsf.helpers.AppConstants;
import qetaa.jsf.helpers.PojoRequester;
import qetaa.jsf.model.cart.ApprovedQuotationItem;
import qetaa.jsf.model.cart.Cart;
import qetaa.jsf.model.location.City;
import qetaa.jsf.model.promotion.PromotionCode;
import qetaa.jsf.model.vehicle.ModelYear;

@Named(value="myOrdersBean")
@SessionScoped
public class MyOrdersBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Cart selectedCart;
	private List<Cart> carts;

	@Inject
	private Requester reqs;

	@Inject
	private LoginBean loginBean;

	@PostConstruct
	public void init() {
		this.selectedCart = new Cart();
		initActiveCarts();
		initCartVariables();
	}

	private void initActiveCarts() {
		Response r = reqs
				.getSecuredRequest(AppConstants.getCustomerQuotationCarts(loginBean.getAccess().getCustomer().getId()));
		if (r.getStatus() == 200) {
			this.carts = r.readEntity(new GenericType<List<Cart>>() {
			});
		} else {
			carts = new ArrayList<>();
		}
	}

	public void initCartVariables() {
		String header = reqs.getSecurityHeader();
		for (Cart cart : carts) {
			cart.setCustomer(this.loginBean.getAccess().getCustomer());
			initModelYear(cart, header);
			initApprovedItems(cart, header);
			initCity(cart, header);
			initPromoCode(cart, header);
		}

	}
	
	private void initPromoCode(Cart cart, String header) {
		try {
			if (cart.getPromotionCode() != null) {
				Response r = PojoRequester.getSecuredRequest(AppConstants.getPromoCode(cart.getPromotionCode()),
						header);
				if (r.getStatus() == 200) { 
					cart.setPromoCodeObject(r.readEntity(PromotionCode.class));
				}
			}

		} catch (Exception ex) {

		}
	}


	private void initModelYear(Cart cart, String header) {
		Response r = PojoRequester.getSecuredRequest(AppConstants.getModelYear(cart.getVehicleYear()), header);
		if (r.getStatus() == 200) {
			ModelYear modelYear = r.readEntity(ModelYear.class);
			cart.setModelYear(modelYear);
		}
	}

	private void initApprovedItems(Cart cart, String header) {
		String link = "";
		if (cart.getStatus() == 'T') {
			link = AppConstants.getCustomerPartsApprovedITems(cart.getId());
		} else {
			link = AppConstants.getCustomerApprovedItems(cart.getId());
		}
		Response r = PojoRequester.getSecuredRequest(link, header);
		if (r.getStatus() == 200) {
			List<ApprovedQuotationItem> approved = r.readEntity(new GenericType<List<ApprovedQuotationItem>>() {
			});
			cart.setApprovedItems(approved);
		}
	}

	private void initCity(Cart cart, String header) {
		Response r = PojoRequester.getSecuredRequest(AppConstants.getCity(cart.getCityId()), header);
		if (r.getStatus() == 200) {
			City city = r.readEntity(City.class);
			cart.setCity(city);
		}
	}

	public Cart getSelectedCart() {
		return selectedCart;
	}

	public void setSelectedCart(Cart selectedCart) {
		this.selectedCart = selectedCart;
	}

	public List<Cart> getCarts() {
		return carts;
	}

	public void setCarts(List<Cart> carts) {
		this.carts = carts;
	}

}
