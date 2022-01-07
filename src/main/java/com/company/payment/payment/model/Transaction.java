package com.company.payment.payment.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;

@Entity
@Table(name = "TRANSACTION")
public class Transaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7610773027616426795L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id", unique = true, nullable = false)
	private Integer transactionId;

	@Column(name = "uuid", unique = false, nullable = false)
	private String uuid = UUID.randomUUID().toString();

	@Min(0)
	@Column(name = "amount", unique = false, nullable = true)
	private Double amount;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", unique = false, nullable = false)
	private TransactionStatus status;

	@Column(name = "customer_email", unique = false, nullable = false)
	@Email(message = "Email should be valid")
	private String customerEmail;

	@Column(name = "customer_phone", unique = false, nullable = false)
	private String customerPhone;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", unique = false, nullable = false)
	private TransactionType type;

	@ManyToOne
	@JoinColumn(name = "merchant_id", unique = true)
	private Merchant merchant;

	public Integer getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

}
