package vn.ptit.models;

public class CustomerDepositStat extends Customer {
	private double deposit;

	public CustomerDepositStat() {

	}

	public CustomerDepositStat(double deposit) {
		super();
		this.deposit = deposit;
	}

	public double getDeposit() {
		return deposit;
	}

	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}

}
