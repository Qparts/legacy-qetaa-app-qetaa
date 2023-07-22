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

import org.primefaces.model.chart.MeterGaugeChartModel;

import qetaa.jsf.beans.reqs.Requester;
import qetaa.jsf.helpers.AppConstants;
import qetaa.jsf.model.customer.LoyaltyGift;
import qetaa.jsf.model.customer.LoyaltyPoints;

@Named
@SessionScoped
public class LoyaltyBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private MeterGaugeChartModel meterGaugeModel;
	private List<LoyaltyPoints> loyaltyPoints;
	private List<LoyaltyGift> loyaltyGifts;

	@Inject
	private Requester reqs;
	@Inject
	private LoginBean loginBean;

	@PostConstruct
	public void init() {
		initLoyaltyPoints();
		initLoyaltyGifts();
		createMeterGaugeModels();
	}
	
	private void initLoyaltyGifts() {
		this.loyaltyGifts = new ArrayList<>();
		Response r = reqs.getSecuredRequest(AppConstants.GET_ACTIVE_LOYALTY_GIFTS);
		if(r.getStatus() == 200) {
			this.loyaltyGifts= r.readEntity(new GenericType<List<LoyaltyGift>>() {});
		}
	}
	
	private void initLoyaltyPoints() {
		Response r = reqs.getSecuredRequest(AppConstants.getLoyaltyPoints(loginBean.getAccess().getCustomer().getId()));
		if(r.getStatus() == 200) {
			this.loyaltyPoints = r.readEntity(new GenericType<List<LoyaltyPoints>>() {});
		}
		else {
			loyaltyPoints = new ArrayList<>();
		}
	}

	public MeterGaugeChartModel getMeterGaugeModel() {
		return meterGaugeModel;
	}

	private void createMeterGaugeModels() {
		meterGaugeModel = initMeterGaugeModel();
		meterGaugeModel.setSeriesColors("efefef,efefef,efefef,efefef");
		meterGaugeModel.setGaugeLabelPosition("bottom");
		meterGaugeModel.setShowTickLabels(true);
	}
	
	public int getTotalPoints() {
		int total = 0;
		for(LoyaltyPoints lp : this.loyaltyPoints) {
			total += lp.getPoints();
		}
		return total;
	}

	private MeterGaugeChartModel initMeterGaugeModel() {
		List<Number> intervals = new ArrayList<Number>();
		intervals.add(50);
		intervals.add(100);
		intervals.add(150);
		intervals.add(200);
		int total = 0;
		for(LoyaltyPoints lp : this.loyaltyPoints) {
			total += lp.getPoints();
		}
		if(total > 200) 
			total = 200;
		return new MeterGaugeChartModel(total, intervals);
	}

	public List<LoyaltyPoints> getLoyaltyPoints() {
		return loyaltyPoints;
	}

	public void setLoyaltyPoints(List<LoyaltyPoints> loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}

	public List<LoyaltyGift> getLoyaltyGifts() {
		return loyaltyGifts;
	}

	public void setLoyaltyGifts(List<LoyaltyGift> loyaltyGifts) {
		this.loyaltyGifts = loyaltyGifts;
	}
	
}
