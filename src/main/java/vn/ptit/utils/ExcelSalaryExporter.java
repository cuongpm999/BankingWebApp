package vn.ptit.utils;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import vn.ptit.models.Customer;
import vn.ptit.models.EmployeeSalary;
import vn.ptit.models.Transaction;

public class ExcelSalaryExporter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<EmployeeSalary> list;
	private String date;

	public ExcelSalaryExporter(List<EmployeeSalary> list, String date) {
		workbook = new XSSFWorkbook();
		this.list = list;
		this.date = date;
	}

	private void writeTitleLine() {
		sheet = workbook.createSheet("Salary");
		Row row = sheet.createRow(0);
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontName("Times New Roman");
		font.setFontHeight(16);
		style.setFont(font);
		createCell(row, 0, "CHI TIẾT LƯƠNG NHÂN VIÊN TRONG THÁNG " + date, style);
		sheet.addMergedRegion(CellRangeAddress.valueOf("A1:L1"));

	}

	private void writeHeaderLine() {
		Row row = sheet.createRow(3);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontName("Times New Roman");
		font.setFontHeight(14);
		style.setFont(font);

		createCell(row, 0, "STT", style);
		createCell(row, 1, "CMT", style);
		createCell(row, 2, "Họ tên", style);
		createCell(row, 3, "Địa chỉ", style);
		createCell(row, 4, "Ngày sinh", style);
		createCell(row, 5, "Vị trí", style);
		createCell(row, 6, "Tổng lương", style);
		createCell(row, 7, "Tháng", style);

	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof Long) {
			cell.setCellValue((Long) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	private void writeDataLines() {
		int rowCount = 4;

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		font.setFontName("Times New Roman");
		style.setFont(font);

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Locale local = new Locale("vi", "VN");
		NumberFormat numberFormat = NumberFormat.getInstance(local);

		int i = 1;
		for (EmployeeSalary employeeSalary : list) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;

			createCell(row, columnCount++, i, style);
			createCell(row, columnCount++, employeeSalary.getIdCard(), style);
			createCell(row, columnCount++, employeeSalary.getFullName(), style);
			createCell(row, columnCount++, employeeSalary.getAddress(), style);
			createCell(row, columnCount++, sdf.format(employeeSalary.getDateOfBirth()), style);
			createCell(row, columnCount++, employeeSalary.getPosition(), style);
			createCell(row, columnCount++, numberFormat.format(employeeSalary.getTotalSalary()), style);
			createCell(row, columnCount++, date, style);
			i++;
		}
	}

	public void export(HttpServletResponse response) throws IOException {
		writeTitleLine();
		writeHeaderLine();
		writeDataLines();

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();

	}

}
