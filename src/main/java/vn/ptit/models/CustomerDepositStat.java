package vn.ptit.models;

import java.util.Date;

public class CustomerDepositStat extends Customer{
	private double deposit;
	
	public CustomerDepositStat(String fullName, String idCard, Date dateOfBirth, String address, double deposit) {
		super(fullName, idCard, dateOfBirth, address);
		this.deposit = deposit;
	}
	
	public CustomerDepositStat() {
		// TODO Auto-generated constructor stub
	}

	public double getDeposit() {
		return deposit;
	}

	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}

}
