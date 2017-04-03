package test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ServletTest extends HttpServlet {


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进来了");
		
		  DiskFileItemFactory diskFactory = new DiskFileItemFactory();  
          // threshold 极限、临界值，即硬盘缓存 1M  
          diskFactory.setSizeThreshold(4 * 1024);  
          // repository 贮藏室，即临时文件目录  
          diskFactory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        
          ServletFileUpload upload = new ServletFileUpload(diskFactory);  
            upload.setFileSizeMax(4*1024*1024);//设置最大文件大小
            try {
                List<FileItem> items=upload.parseRequest(req);//获取所有表单
                System.out.print(items.size());
                for(FileItem item:items){
                    //判断当前的表单控件是否是一个普通控件
                    if(!item.isFormField()){
                        //是一个文件控件时
                        String excelFileName = new String(item.getName().getBytes(), "utf-8"); //获取上传文件的名称
                        //上传文件必须为excel类型,根据后缀判断(xls)
                        String excelContentType = excelFileName.substring(excelFileName.lastIndexOf(".")); //获取上传文件的类型
                        System.out.println("上传文件名:"+excelFileName);
                        System.out.println("文件大小:"+item.getSize());
                        System.out.println("\n---------------------------------------");
                        if(".xls".equals(excelContentType)){
                            POIFSFileSystem fileSystem = new POIFSFileSystem(item.getInputStream());
                            HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
                            System.out.println(workbook);
                            HSSFSheet sheet = workbook.getSheetAt(0);
                            int rows = sheet.getPhysicalNumberOfRows();
                            for (int i = 0; i < rows; i++) {
                                HSSFRow row = sheet.getRow(i);
                                int columns = row.getPhysicalNumberOfCells();
                                for (int j = 0; j < columns; j++) {
                                    HSSFCell cell = row.getCell(j);
                                    String value = cell.getStringCellValue();
                                    System.out.print(value + "|");
                                }
                                System.out.println("\n---------------------------------------");
                            }
                            System.out.println("success！");
                        }else{
                            System.out.println("必须为excel类型");
                        }
                        //顺便把文件保存到硬盘,防止重名
//                        String newName=new SimpleDateFormat("yyyyMMDDHHmmssms").format(new Date());
//                        File file = new File("d:/upload");
//                        if(!file.exists()){
//                            file.mkdir();
//                        }
//                        item.write(new File("d:/upload/"+newName+excelContentType));
                        resp.sendRedirect("index.jsp");
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            

        }
		
	
		
		
       
	
    
}
