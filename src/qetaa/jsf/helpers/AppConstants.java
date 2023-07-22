package qetaa.jsf.helpers;
 
public class AppConstants {
	//===============HOST=================//
	public final static String GIFTS_DIRECTORY_WINDOWS = "C:\\loyalty-gifts-pics\\";
	public final static String GIFTS_DIRECTORY_LINUX = "/home/ubuntu/loyalty-gifts-pics/";
	public final static String VIN_DIRECTORY_WINDOWS = "C:\\VIN-DIR\\";
	public final static String VIN_DIRECTORY_LINUX = "/home/ubuntu/VIN-DIR/";
	public final static String SERVICE_HOST = "http://localhost:8081";
	public final static String APP_SECRET = "093X3b*y&iWu4U&F181X#3ZE0)%9374";
	private final static String OS = System.getProperty("os.name").toLowerCase();
	
//	public final static String APP_HOST = "http://localhost:8081/jsf";
	//public final static String APP_HOST_UNSECURE = "http://localhost:8081/jsf";
	//public final static String HOST_ESCAPED = "http%3A%2F%2Flocalhost:8081%2Fjsf%2F";
	
	//for server
	public final static String APP_HOST = "http://52.54.251.161:8080/";
	public final static String APP_HOST_UNSECURE = "http://52.54.251.161:8080/";
	public final static String HOST_ESCAPED = "http://52.54.251.161:8080/";
	
	//===============SERVICES=================//
	private final static String CUSTOMER_SERVICE = SERVICE_HOST + "/service-qetaa-customer/rest/";
	private final static String VEHICLES_SERVICE = SERVICE_HOST +"/service-qetaa-vehicle/rest/";
	private final static String CART_SERVICE =SERVICE_HOST +"/service-qetaa-cart/rest/";
	private final static String LOCATION_SERVICE = SERVICE_HOST + "/service-qetaa-location/rest/";
	private final static String PAYMENT_SERVICE_MOYASAR = SERVICE_HOST +"/service-qetaa-payment/rest/moyasar/";
	private final static String PAYMENT_SERVICE = SERVICE_HOST +"/service-qetaa-payment/rest/";
	private final static String PAYMENT_SERVICE_BANKS = SERVICE_HOST  +"/service-qetaa-payment/rest/banks/";
	private final static String USER_SERVICE = SERVICE_HOST +"/service-qetaa-user/rest/";
	private final static String PROMOTION_SERVICE = SERVICE_HOST + "/service-qetaa-vendor/rest/promotion/";
	private final static String VENDOR_SERVICE = SERVICE_HOST + "/service-qetaa-vendor/rest/";
	
	//===============FACEBOOK=================//
	public static final String FB_APP_ID = "156876888175917";
	public static final String FB_APP_SECRET = "85a0414cf6c11e42f6271c2822e45c9c";
	public static final String FB_REDIRECT_URL = HOST_ESCAPED + "successful";
	public static final String FB_SCOPE = "public_profile%2Cemail";//public profile and email
	public static final String FB_DIALOG_URL = "https://www.facebook.com/dialog/oauth?client_id="+FB_APP_ID+"&redirect_uri="+FB_REDIRECT_URL+"&scope="+FB_SCOPE;
	public final static String PAGE_SUCCESSFUL = APP_HOST + "successful";
	
	//===============LOGIN AND REGISTERATION=================//
	public final static String POST_REGISTER_SMS = CUSTOMER_SERVICE + "register-sms";
	public final static String POST_REGISTER_EMAIL = CUSTOMER_SERVICE + "register-email";
	public final static String POST_MOBILE_REGISTER = CUSTOMER_SERVICE + "mobile-register";
	public final static String POST_EMAIL_REGISTER = CUSTOMER_SERVICE + "email-register";
	public final static String POST_FACEBOOK_LOGIN = CUSTOMER_SERVICE + "facebook-login";
	public final static String POST_FACEBOOK_REGISTER = CUSTOMER_SERVICE + "facebook-register";
	public final static String POST_EMAIL_LOGIN = CUSTOMER_SERVICE + "email-login";
	public final static String POST_RESET_SMS = CUSTOMER_SERVICE + "reset-sms";
	public final static String PUT_RESET_PASSWORD = CUSTOMER_SERVICE + "reset-password";
	public final static String GET_ACTIVE_COUNTRIES = LOCATION_SERVICE + "active-countries-customers";

	//===============MAKES=================//
	public final static String GET_ACTIVE_MAKES = VEHICLES_SERVICE + "all-active-makes";
	//===============VENDORS====================//
	public final static String POST_JOIN_US = VENDOR_SERVICE + "vendor-join-request";
	
	//===============CARTS=================//
	public final static String POST_CREATE_CART = CART_SERVICE + "cart";
	public final static String PUT_UPDATE_CART = CART_SERVICE + "cart";  
	//===============PARTS=================//
	public final static String POST_WIRE_TRASNFER = CART_SERVICE + "wire-transfer";
	public final static String POST_PARTS_PAYMENT_MAYASAR = PAYMENT_SERVICE_MOYASAR + "payment-request";
	public final static String POST_PARTS_PAYMENT_FINALIZE = PAYMENT_SERVICE+ "save-successful-payment/customer";
	public final static String CUSTOMER_CREATE_PARTS_ORDER = CART_SERVICE + "parts-order";
	public final static String CUSTOMER_GET_ACTIVE_BANKS = PAYMENT_SERVICE_BANKS + "active-banks/customer";
	
	public final static String POST_HIT_COUNTER = CUSTOMER_SERVICE + "hit";
	public final static String POST_HIT_ACTIVITIES = CUSTOMER_SERVICE + "hit-activities";
	public final static String GET_NEW_VISIT_INDEX = CUSTOMER_SERVICE + "new-visit-index";
	
	public final static String USER_MATCH_TOKEN = USER_SERVICE + "match-token";
	
	public final static String GET_ACTIVE_LOYALTY_GIFTS = CUSTOMER_SERVICE + "active-loyalty-gifts";
		
	public final static String getCodeLogin(String code) {
		return CUSTOMER_SERVICE + "code-login/code/"+code;
	}
	
	public final static String getCountryCities(int countryId) {
		return LOCATION_SERVICE + "active-cities-customer/country/" + countryId;
	}
	
	public final static String getCountryRegions(int countryId) {
		return LOCATION_SERVICE + "active-region-cities-customer/country/" + countryId;
	}
	
	public final static String getCustomerQuotationCarts(long customerId){
		return CART_SERVICE + "customer-quotation-carts/customer/" + customerId;
	}
	
	public final static String getCustomerArchivedCarts(long customerId){
		return CART_SERVICE + "customer-past-carts/customer/" + customerId;
	}
	
	public final static String getModelYear(int modelYearId) {
		return VEHICLES_SERVICE + "model-year/" + modelYearId;
	}
	
	public final static String getCity(long cityId) {
		return LOCATION_SERVICE + "city/" + cityId;
	}
	
	public final static String getCustomerApprovedItems(long cartId){
		return CART_SERVICE + "customer-approved-items/cart/" + cartId;
	}
	
	public final static String getCustomerPartsApprovedITems(long cartId) {
		return CART_SERVICE + "customer-parts-approved-items/cart/" + cartId;
	}
	
	
	public final static String getQuotationCart(long cartId, long customerId){
		return CART_SERVICE + "quotation-cart/" + cartId + "/customer/" + customerId;
	}
	
	public final static String getLoyaltyPoints(long customerId) {
		return CUSTOMER_SERVICE + "loyalty-points/customer/" + customerId;
	}
	
	public final static String getGiftsDirectory() {
		if (OS.indexOf("win") >= 0) {
			return GIFTS_DIRECTORY_WINDOWS;
		} else {
			return GIFTS_DIRECTORY_LINUX;
		}
	} 
	
	public final static String getVINDirectory() {
		if (OS.indexOf("win") >= 0) {
			return VIN_DIRECTORY_WINDOWS;
		} else {
			return VIN_DIRECTORY_LINUX;
		}
	}
	
	public final static String getVINDirectoryWithDate(int year, int month, int day) {
		if (OS.indexOf("win") >= 0) {
			return VIN_DIRECTORY_WINDOWS + year +  "\\" + month + "\\" + day + "\\";
		} else {
			return VIN_DIRECTORY_LINUX + year + "/" + month + "/" + day + "/";
		}
	}
	
	
	public final static String getPromotionCodeFromCode(String code) {
		return PROMOTION_SERVICE + "promotion-code/code/" + code;
	}
	
	public final static String getPromoCode(long promoCodeId) {
		return PROMOTION_SERVICE  + "promotion-code/" + promoCodeId;
	}
	
}
