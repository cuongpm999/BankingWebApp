package vn.ptit.utils;

import vn.ptit.models.CreatedBankAccount;
import vn.ptit.models.CreditAccount;
import vn.ptit.models.DepositAccount;

public class HelperCreateBankAccount {
	private CreditAccount creditAccount;
	private DepositAccount depositAccount;
	private CreatedBankAccount createdBankAccount;
	
	public HelperCreateBankAccount() {
		// TODO Auto-generated constructor stub
	}

	public HelperCreateBankAccount(CreditAccount creditAccount, DepositAccount depositAccount,
			CreatedBankAccount createdBankAccount) {
		super();
		this.creditAccount = creditAccount;
		this.depositAccount = depositAccount;
		this.createdBankAccount = createdBankAccount;
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

	public CreatedBankAccount getCreatedBankAccount() {
		return createdBankAccount;
	}

	public void setCreatedBankAccount(CreatedBankAccount createdBankAccount) {
		this.createdBankAccount = createdBankAccount;
	}

}
