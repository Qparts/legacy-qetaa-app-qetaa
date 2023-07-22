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
import qetaa.jsf.model.payment.Bank;

@Named(value="bankBean")
@SessionScoped
public class BankBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<Bank> banks;
	@Inject
	private Requester reqs;
	
	@PostConstruct
	private void init() {
		initBanks();
	}
	
	private void initBanks() {
		banks = new ArrayList<>();
		Response r = reqs.getSecuredRequest(AppConstants.CUSTOMER_GET_ACTIVE_BANKS);
		if(r.getStatus() == 200) {
			this.banks= r.readEntity(new GenericType<List<Bank>>() {});
		}
	}
	

	public List<Bank> getBanks() {
		return banks;
	}

	public void setBanks(List<Bank> banks) {
		this.banks = banks;
	}


}
