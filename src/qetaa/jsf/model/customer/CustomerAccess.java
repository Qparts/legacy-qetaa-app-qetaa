package qetaa.jsf.model.customer;

import java.io.Serializable;

public class CustomerAccess implements Serializable{
	private static final long serialVersionUID = 1L;
	private Customer customer;
	private String token;
	private long cartId; 
	
	public Customer getCustomer() {
		return customer;
	}
	public String getToken() {
		return token;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public void setToken(String accessToken) {
		this.token = accessToken;
	}
	public long getCartId() {
		return cartId;
	}
	public void setCartId(long cartId) {
		this.cartId = cartId;
	}

}
