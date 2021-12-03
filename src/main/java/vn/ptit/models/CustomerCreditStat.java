package vn.ptit.models;

public class CustomerCreditStat extends Customer{
	private double credit;
	
	public CustomerCreditStat() {

	}

	public CustomerCreditStat(double credit) {
		super();
		this.credit = credit;
	}
	
	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

}
