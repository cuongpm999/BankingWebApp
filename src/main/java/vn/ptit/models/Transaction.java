package vn.ptit.models;

import java.util.Date;

public class Transaction {
	private int id;
	private String type;
	private Date dateCreate;
	private double money;
	private double afterBalanceDeposit;
	private double afterBalanceCredit;
	private String note;
	private Customer customer;
	private Employee employee;
	private CreditAccount creditAccount;
	private DepositAccount depositAccount;

	public Transaction() {
		
	}

	public Transaction(Date dateCreate, double money, double afterBalanceDeposit, double afterBalanceCredit,
			String note, Customer customer, Employee employee, CreditAccount creditAccount,
			DepositAccount depositAccount) {
		super();
		this.dateCreate = dateCreate;
		this.money = money;
		this.afterBalanceDeposit = afterBalanceDeposit;
		this.afterBalanceCredit = afterBalanceCredit;
		this.note = note;
		this.customer = customer;
		this.employee = employee;
		this.creditAccount = creditAccount;
		this.depositAccount = depositAccount;
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

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public double getAfterBalanceDeposit() {
		return afterBalanceDeposit;
	}

	public void setAfterBalanceDeposit(double afterBalanceDeposit) {
		this.afterBalanceDeposit = afterBalanceDeposit;
	}

	public double getAfterBalanceCredit() {
		return afterBalanceCredit;
	}

	public void setAfterBalanceCredit(double afterBalanceCredit) {
		this.afterBalanceCredit = afterBalanceCredit;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public CreditAccount getCreditAccount() {
		return creditAccount;
	}

	public void setCreditAccount(CreditAccount creditAccount) {
		this.creditAccount = creditAccount;
	}

	public DepositAccount getDepositAccount() {
		return depositAccount;
	}

	public void setDepositAccount(DepositAccount depositAccount) {
		this.depositAccount = depositAccount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
