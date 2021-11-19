package vn.ptit.models;

public class BankAccount {
	private String id;
	private double balance;
	private BankAccountType bankAccountType;

	public BankAccount() {
		// TODO Auto-generated constructor stub
	}

	public BankAccount(String id, double balance, BankAccountType bankAccountType) {
		super();
		this.id = id;
		this.balance = balance;
		this.bankAccountType = bankAccountType;
	}

	public BankAccountType getBankAccountType() {
		return bankAccountType;
	}

	public void setBankAccountType(BankAccountType bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

}
