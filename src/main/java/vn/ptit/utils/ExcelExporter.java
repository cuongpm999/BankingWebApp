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
import vn.ptit.models.Transaction;

public class ExcelExporter {
	private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Transaction> list;
    private Customer customer;
    private String idBankAccount;
    private int index;
    
    public ExcelExporter(List<Transaction> list, Customer customer, String idBankAccount, int index) {
    	workbook = new XSSFWorkbook();
    	this.list = list;
    	this.customer = customer;
    	this.idBankAccount = idBankAccount;
    	this.index = index;
	}
    
    private void writeTitleLine() {
        sheet = workbook.createSheet("Transaction");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontName("Times New Roman"); 
        font.setFontHeight(16);
        style.setFont(font);     
        createCell(row, 0, "CHI TIẾT GIAO DỊCH TÀI KHOẢN KHÁCH HÀNG", style); 
        sheet.addMergedRegion(CellRangeAddress.valueOf("A1:L1"));
         
    }
    
    private void writeCustomerLine() {
        Row row = sheet.createRow(3);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontName("Times New Roman"); 
        font.setFontHeight(14);
        style.setFont(font);     
        createCell(row, 0, "Khách hàng", style); 
        sheet.addMergedRegion(CellRangeAddress.valueOf("A4:B4"));
        
        CellStyle styleNormal = workbook.createCellStyle();
        XSSFFont fontNormal = workbook.createFont();
        fontNormal.setBold(false);
        fontNormal.setFontName("Times New Roman"); 
        fontNormal.setFontHeight(14);
        styleNormal.setFont(fontNormal); 
        createCell(row, 2, customer.getFullName(), styleNormal); 
        sheet.addMergedRegion(CellRangeAddress.valueOf("C4:K4"));
        
        row = sheet.createRow(4);
        createCell(row, 0, "Email", style); 
        sheet.addMergedRegion(CellRangeAddress.valueOf("A5:B5"));
        createCell(row, 2, customer.getEmail(), styleNormal); 
        sheet.addMergedRegion(CellRangeAddress.valueOf("C5:K5"));
        
        row = sheet.createRow(5);
        createCell(row, 0, "Mã tài khoản", style); 
        sheet.addMergedRegion(CellRangeAddress.valueOf("A6:B6"));
        createCell(row, 2, idBankAccount, styleNormal); 
        sheet.addMergedRegion(CellRangeAddress.valueOf("C6:K6"));
         
    }
 
    private void writeHeaderLine() {
        Row row = sheet.createRow(7);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontName("Times New Roman"); 
        font.setFontHeight(14);
        style.setFont(font);
         
        createCell(row, 0, "STT", style);      
        createCell(row, 1, "Loại giao dịch", style);       
        createCell(row, 2, "Ngày thực hiện", style);    
        createCell(row, 3, "Số tiền giao dịch", style);
        if(index==1) createCell(row, 4, "Số nợ sau giao dịch", style);
        if(index==2) createCell(row, 4, "Số dư sau giao dịch", style);
        createCell(row, 5, "Nội dung", style);
         
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
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
     
    private void writeDataLines() {
        int rowCount = 8;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        font.setFontName("Times New Roman"); 
        style.setFont(font);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Locale local = new Locale("vi", "VN");
		NumberFormat numberFormat = NumberFormat.getInstance(local);
        
        int i=1;
        for (Transaction tran : list) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            
            createCell(row, columnCount++, i, style);
            createCell(row, columnCount++, tran.getType(), style);
            createCell(row, columnCount++, sdf.format(tran.getDateCreate()), style);
            createCell(row, columnCount++, numberFormat.format(tran.getMoney()), style);
            if(index==1) createCell(row, columnCount++, numberFormat.format(tran.getAfterBalanceCredit()), style);
            if(index==2) createCell(row, columnCount++, numberFormat.format(tran.getAfterBalanceDeposit()), style);
            createCell(row, columnCount++, tran.getNote(), style);
            i++;
        }
    }
     
    public void export(HttpServletResponse response) throws IOException {
    	writeTitleLine();
        writeHeaderLine();
        writeDataLines();
        writeCustomerLine();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }

    
    
}
