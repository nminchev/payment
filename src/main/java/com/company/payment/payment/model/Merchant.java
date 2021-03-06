package com.company.payment.payment.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;

@Entity
@Table(name = "MERCHANT")
public class Merchant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1710486207340425799L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "merchant_id", unique = true, nullable = false)
	private Integer merchantId;

	@Column(name = "name", unique = false, nullable = false)
	private String name;

	@Column(name = "description", unique = false, nullable = true)
	private String description;

	@Column(name = "email", unique = true, nullable = false)
	@Email(message = "Email should be valid")
	private String email;

	@Column(name = "status", unique = false, nullable = false)
	@Enumerated(EnumType.STRING)
	private MerchantStatus status;

	@Column(name = "total_transaction_sum", unique = false, nullable = false)
	private Double totalTransactionSum;

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public MerchantStatus getStatus() {
		return status;
	}

	public void setStatus(MerchantStatus status) {
		this.status = status;
	}

	public Double getTotalTransactionSum() {
		return totalTransactionSum;
	}

	public void setTotalTransactionSum(Double totalTransactionSum) {
		this.totalTransactionSum = totalTransactionSum;
	}
}
