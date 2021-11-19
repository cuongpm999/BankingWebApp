package vn.ptit.models;

import java.util.Date;

public class CreatedBankAccount {
	private int id;
	private Date dateCreate;
	private BankAccount bankAccount;
	private Employee employee;
	private Customer customer;

	public CreatedBankAccount() {
		super();
	}

	public CreatedBankAccount(Date dateCreate, BankAccount bankAccount, Employee employee, Customer customer) {
		super();
		this.dateCreate = dateCreate;
		this.bankAccount = bankAccount;
		this.employee = employee;
		this.customer = customer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(Date dateCreate) {
		this.dateCreate = dateCreate;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
