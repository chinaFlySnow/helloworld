package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExcelExport {

	@RequestMapping("excelExport.do")
	public void excelExport(HttpServletResponse response,String page) throws IOException{
		System.out.println("excelE小port方法触发了"+page);
		
		response.setContentType("application/msexcel;");                
        response.setHeader("Content-Disposition", new String(("attachment;filename="+"excelExport.xlsx").getBytes("GB2312"), "UTF-8"));  

        InputStream in = ExcelExport.class.getClassLoader().getResourceAsStream("shiyishi.xlsx");  
        
        Workbook wb = new XSSFWorkbook(in);
        Sheet sheet=wb.getSheetAt(0);
        Row row=sheet.createRow(1);
        Cell cell=row.createCell(4);
        cell.setCellValue("看看是不是好了");
        
        OutputStream os=response.getOutputStream();
        wb.write(os);
        in.close();
        os.close();
        
        
        Map cityCompany = new HashMap();
        cityCompany.put("001", "北京");
        cityCompany.put("002", "上海");
        cityCompany.put("003", "南京");
        cityCompany.put("004", "开封");
        
       String city=(String) cityCompany.get("004");
        System.out.println(city);
	}
}
