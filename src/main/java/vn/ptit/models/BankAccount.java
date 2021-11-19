package vn.ptit.models;

public class BankAccount {
	private String id;
	private double balance;
	public BankAccount() {
		// TODO Auto-generated constructor stub
	}
	public BankAccount(double balance) {
		super();
		this.balance = balance;
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
