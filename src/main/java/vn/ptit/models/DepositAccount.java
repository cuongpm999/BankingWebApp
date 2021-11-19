package vn.ptit.models;

public class DepositAccount extends BankAccount{
	private double interestRate;
	private double minimumBalance;
	public DepositAccount() {
		// TODO Auto-generated constructor stub
	}
	public DepositAccount(double balance, double interestRate, double minimumBalance) {
		super(balance);
		this.interestRate = interestRate;
		this.minimumBalance = minimumBalance;
	}
	public double getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
	public double getMinimumBalance() {
		return minimumBalance;
	}
	public void setMinimumBalance(double minimumBalance) {
		this.minimumBalance = minimumBalance;
	}
	
}
