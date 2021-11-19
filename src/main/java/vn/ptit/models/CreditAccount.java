package vn.ptit.models;

public class CreditAccount extends BankAccount{
	private double limitBalance;
	public CreditAccount() {
		// TODO Auto-generated constructor stub
	}
	public CreditAccount(double balance, double limitBalance) {
		super(balance);
		this.limitBalance = limitBalance;
	}
	public double getLimitBalance() {
		return limitBalance;
	}
	public void setLimitBalance(double limitBalance) {
		this.limitBalance = limitBalance;
	}
	
}
