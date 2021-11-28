package vn.ptit.models;

import java.util.Date;

public class Salary {
	private int id;
	private double basicSalary;
	private double bonusSalary;
	private String dateSalary;
	
	public Salary() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(double basicSalary) {
		this.basicSalary = basicSalary;
	}

	public double getBonusSalary() {
		return bonusSalary;
	}

	public void setBonusSalary(double bonusSalary) {
		this.bonusSalary = bonusSalary;
	}

	public String getDateSalary() {
		return dateSalary;
	}

	public void setDateSalary(String dateSalary) {
		this.dateSalary = dateSalary;
	}
}
