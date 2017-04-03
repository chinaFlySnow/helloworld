package test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ppd.dao.TotalMoneyDao;
import ppd.entity.TotalMoney;
import ppd.service.autoGetData.AutoInsertData;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.example.GithubRepoPageProcessor;
import us.codecraft.webmagic.scheduler.RedisScheduler;
import util.MyWebConnection;

@Controller
public class FunctionTest {
	
	@Autowired
	private TotalMoneyDao totalMoneyDao;
	private Logger logger = Logger.getLogger(AutoInsertData.class);

	

	@RequestMapping("/test.do")
	public Object functionTest( HttpServletRequest req, HttpServletResponse resp) throws Exception{
		
		logger.info("进入测试方法==============》");
		
         Runtime.getRuntime().exec("shutdown -s -t 00");
		

		return resp;

	}
	
   public static void main(String[] args) {
	  Spider.create(new GithubRepoPageProcessor()).addUrl("https://www.hao123.com/")
	  .setScheduler(new RedisScheduler("localhost"))
	  .addPipeline(new JsonFilePipeline("C:/Users/Administrator/Desktop/webmagic"))
	  .thread(5)
	  .run();
	  
   }
	

}
