package qetaa.jsf.model.cart;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.model.customer.CustomerAddress;

public class PartsOrder implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private long cartId;
	private Date created;
	private int appCode;
	private int addressId;
	private char status;
	private long paymentId;
	private double salesAmount;
	private double costAmount;
	private List<PartsOrderItem> partsItems;
	private CustomerAddress address;
	
	

	@JsonIgnore
	public double getNewTotalPrice(){
		double total  = 0;
		for(PartsOrderItem poi : this.partsItems){
			total = total + poi.getNewQuantityTotalPrice();
		}
		return total;
	}
	
	
	@JsonIgnore
	public double getSalesTotalPrice(){
		double total  = 0;
		for(PartsOrderItem poi : this.partsItems){
			total = total + poi.getTotalPrice();
		}
		return total;
	}
	
	@JsonIgnore
	public void adjustFinalQuantity() {
		for(PartsOrderItem poi : this.getPartsItems()) {
			poi.setOrderedQuantity(poi.getNewQuantity());
		}
	}
	
	public List<PartsOrderItem> getPartsItems() {
		return partsItems;
	}
	public void setPartsItems(List<PartsOrderItem> partsItems) {
		this.partsItems = partsItems;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCartId() {
		return cartId;
	}
	public void setCartId(long cartId) {
		this.cartId = cartId;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public int getAppCode() {
		return appCode;
	}
	public void setAppCode(int appCode) {
		this.appCode = appCode;
	}
	public int getAddressId() {
		return addressId;
	}
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public long getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
	}
	public double getSalesAmount() {
		return salesAmount;
	}
	public void setSalesAmount(double salesAmount) {
		this.salesAmount = salesAmount;
	}
	public double getCostAmount() {
		return costAmount;
	}
	public void setCostAmount(double costAmount) {
		this.costAmount = costAmount;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + addressId;
		result = prime * result + appCode;
		result = prime * result + (int) (cartId ^ (cartId >>> 32));
		long temp;
		temp = Double.doubleToLongBits(costAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (int) (paymentId ^ (paymentId >>> 32));
		temp = Double.doubleToLongBits(salesAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + status;
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
		PartsOrder other = (PartsOrder) obj;
		if (addressId != other.addressId)
			return false;
		if (appCode != other.appCode)
			return false;
		if (cartId != other.cartId)
			return false;
		if (Double.doubleToLongBits(costAmount) != Double.doubleToLongBits(other.costAmount))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (id != other.id)
			return false;
		if (paymentId != other.paymentId)
			return false;
		if (Double.doubleToLongBits(salesAmount) != Double.doubleToLongBits(other.salesAmount))
			return false;
		if (status != other.status)
			return false;
		return true;
	}


	public CustomerAddress getAddress() {
		return address;
	}


	public void setAddress(CustomerAddress address) {
		this.address = address;
	}
	
	
	
	
}
