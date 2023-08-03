package qetaa.jsf.model.cart;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import qetaa.jsf.model.customer.Customer;
import qetaa.jsf.model.location.City;
import qetaa.jsf.model.promotion.PromotionCode;
import qetaa.jsf.model.vehicle.ModelYear;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Cart implements Serializable{
	private static final long serialVersionUID = 1L;
	private long id;
	private long customerId;
	private char status;
	private String vin;
	private Date created, submitted;
	private Integer createdBy, submitteBy, cityId, appCode, vehicleYear, makeId, deliveryFees, promotionCode;
	private double vatPercentage;
	private List<CartItem> cartItems;	
	private ModelYear modelYear;
	private Customer customer;
	private boolean noVin;
	private boolean vinImage;
	@JsonIgnore
	private PromotionCode promoCodeObject;
	@JsonIgnore
	private List<CartReview> reviews;
	@JsonIgnore
	private City city;
	@JsonIgnore
	private List<ApprovedQuotationItem> approvedItems;

	
	
	public List<CartReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<CartReview> reviews) {
		this.reviews = reviews;
	}

	@JsonIgnore
	public double getTotalSales() {
		double total = 0;
		for(ApprovedQuotationItem approved : approvedItems) {
			total += approved.getTotalSales();
		}
		return total;
	}

	public ModelYear getModelYear() {
		return modelYear;
	}
	public void setModelYear(ModelYear modelYear) {
		this.modelYear = modelYear;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public Integer getDeliveryFees() {
		return deliveryFees;
	}
	public void setDeliveryFees(Integer deliveryFees) {
		this.deliveryFees = deliveryFees;
	}
	public List<CartItem> getCartItems() {
		return cartItems;
	}
	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getSubmitted() {
		return submitted;
	}
	public void setSubmitted(Date submitted) {
		this.submitted = submitted;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Integer getSubmitteBy() {
		return submitteBy;
	}
	public void setSubmitteBy(Integer submitteBy) {
		this.submitteBy = submitteBy;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public Integer getAppCode() {
		return appCode;
	}
	public void setAppCode(Integer appCode) {
		this.appCode = appCode;
	}
	public Integer getVehicleYear() {
		return vehicleYear;
	}
	public void setVehicleYear(Integer vehicleYear) {
		this.vehicleYear = vehicleYear;
	}
	public Integer getMakeId() {
		return makeId;
	}
	public void setMakeId(Integer makeId) {
		this.makeId = makeId;
	}
	public double getVatPercentage() {
		return vatPercentage;
	}
	public void setVatPercentage(double vatPercentage) {
		this.vatPercentage = vatPercentage;
	}
	public List<ApprovedQuotationItem> getApprovedItems() {
		return approvedItems;
	}
	public void setApprovedItems(List<ApprovedQuotationItem> approvedItems) {
		this.approvedItems = approvedItems;
	}

	public boolean isNoVin() {
		return noVin;
	}

	public void setNoVin(boolean noVin) {
		this.noVin = noVin;
	}

	public boolean isVinImage() {
		return vinImage;
	}

	public void setVinImage(boolean vinImage) {
		this.vinImage = vinImage;
	}

	public Integer getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(Integer promotionCode) {
		this.promotionCode = promotionCode;
	}
	

	public PromotionCode getPromoCodeObject() {
		return promoCodeObject;
	} 

	public void setPromoCodeObject(PromotionCode promoCodeObject) {
		this.promoCodeObject = promoCodeObject;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cart other = (Cart) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
	
}
