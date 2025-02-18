package qetaa.jsf.model.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;


@JsonSubTypes({
    @JsonSubTypes.Type(value = PaymentResponseSadad.class, name = "source"),
    @JsonSubTypes.Type(value = PaymentResponseCC.class, name = "source")
})
public class PaymentResponse {
	
	private String id;//paymentId
	private String status;//inititated
	private Integer amount;//in halalas
	private Integer fee;//transaction fees in halalas
	private String currency;//SAR
	private Integer refunded;//0 in payment
	@JsonProperty("refunded_at")
	private String refundedAt;//null is default
	private String description;//payment desc
	@JsonProperty("amount_format")
	private String amountFormat;
	@JsonProperty("fee_format")
	private String feeFormat;
	@JsonProperty("refunded_format")
	private String refundedFormat;
	@JsonProperty("invoice_id")
	private String invoiceId;//null is default
	private String ip;//null is default
	@JsonProperty("callback_url")
	private String callback;//callbackURL for client
	@JsonProperty("created_at")
	private String createdAt;//creation timestamp in ISO 8601 format.
	@JsonProperty("updated_at")
	private String updatedAt;//modification timestamp in ISO 8601 format.
	
	
	public String getId() {
		return id;
	}
	public String getStatus() {
		return status;
	}
	public Integer getAmount() {
		return amount;
	}
	public Integer getFee() {
		return fee;
	}
	public String getCurrency() {
		return currency;
	}
	public Integer getRefunded() {
		return refunded;
	}
	public String getRefundedAt() {
		return refundedAt;
	}
	public String getDescription() {
		return description;
	}
	public String getInvoiceId() {
		return invoiceId;
	}
	public String getIp() {
		return ip;
	}
	public String getCallback() {
		return callback;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setId(String id) {
		this.id = id;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public void setFee(Integer fee) {
		this.fee = fee;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public void setRefunded(Integer refunded) {
		this.refunded = refunded;
	}
	public void setRefundedAt(String refundedAt) {
		this.refundedAt = refundedAt;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getAmountFormat() {
		return amountFormat;
	}
	public void setAmountFormat(String amountFormat) {
		this.amountFormat = amountFormat;
	}
	public String getFeeFormat() {
		return feeFormat;
	}
	public void setFeeFormat(String feeFormat) {
		this.feeFormat = feeFormat;
	}
	public String getRefundedFormat() {
		return refundedFormat;
	}
	public void setRefundedFormat(String refundedFormat) {
		this.refundedFormat = refundedFormat;
	}
	@Override
	public String toString() {
		return "PaymentResponse [id=" + id + ", status=" + status + ", amount=" + amount + ", fee=" + fee
				+ ", currency=" + currency + ", refunded=" + refunded + ", refundedAt=" + refundedAt + ", description="
				+ description + ", amountFormat=" + amountFormat + ", feeFormat=" + feeFormat + ", refundedFormat="
				+ refundedFormat + ", invoiceId=" + invoiceId + ", ip=" + ip + ", callback=" + callback + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
	
}

