package vn.ptit.models;

import java.util.Date;
import java.util.List;

public class EmployeeSalary extends Employee{
	private double totalSalary;

	public EmployeeSalary() {
		
	}
	
	public EmployeeSalary(String idCard, String fullName, Date dateOfBirth, String address, int level, int seniority,
			String position, double basicSalary, Account account, List<Salary> salaries, boolean status,
			double totalSalary) {
		super(idCard, fullName, dateOfBirth, address, level, seniority, position, basicSalary, account, salaries,
				status);
		this.totalSalary = totalSalary;
	}

	public double getTotalSalary() {
		return totalSalary;
	}

	public void setTotalSalary(double totalSalary) {
		this.totalSalary = totalSalary;
	}
	
}
