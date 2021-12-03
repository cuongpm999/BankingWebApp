package vn.ptit.models;

import java.util.Date;

public class Customer {
	private int id;
	private String fullName;
	private String idCard;
	private Date dateOfBirth;
	private String address;
	private boolean status;
	private String email;

	public Customer() {

	}

	public Customer(String fullName, String idCard, Date dateOfBirth, String address, String email) {
		super();
		this.fullName = fullName;
		this.idCard = idCard;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
