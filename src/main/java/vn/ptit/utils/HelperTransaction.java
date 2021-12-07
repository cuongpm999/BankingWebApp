package vn.ptit.utils;

import vn.ptit.models.Transaction;

public class HelperTransaction {
	private int status;
	private Transaction transaction;
	
	public HelperTransaction() {
		// TODO Auto-generated constructor stub
	}
	
	public HelperTransaction(int status, Transaction transaction) {
		super();
		this.status = status;
		this.transaction = transaction;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	
	

}
