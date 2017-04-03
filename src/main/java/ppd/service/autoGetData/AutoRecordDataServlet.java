package ppd.service.autoGetData;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.Logger;

import ppd.dao.MyWebSignDao;
import ppd.entity.MyWebSign;


public class AutoRecordDataServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AutoInsertData auto = new AutoInsertData();
	private MyWebSignService mws = new MyWebSignService();
    private Logger logger = Logger.getLogger(AutoRecordDataServlet.class);
    
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		Thread thread = new Thread(new Runnable() {
			public void run() {
				logger.info("进入自动线程的run方法==============》");
				SimpleDateFormat sdf = new SimpleDateFormat("HH");
				boolean hadRunned = false;
				while(true){
					try {
						//每天10点自动开启电脑获取需要的数据
					    Date currentTime = new Date();
					    int currentHour = Integer.valueOf(sdf.format(currentTime));
					    if(currentHour==8 && !hadRunned){
					    	//检测是否今天有签到
					    	MyWebSign myWebSign = mws.searchSignByTodayTime();
					    	//插入数据
					    	if(myWebSign == null){
					    		logger.info("今日系统未签到");
					    		auto.autoInsert();	
					    		//今天已经插入签到
						    	mws.insertMyWebSign();
						    	
						    	Runtime.getRuntime().exec("shutdown -s -t 1800");
					    	}else{
					    		logger.info("今日系统已经签到，无需再签到");
					    	}
					    	
					    	hadRunned = true;
					    }
					    if(currentHour==9 && hadRunned){
					    	hadRunned = false;
					    }
					  //每隔1分钟查看下时间  
					  Thread.sleep(1000);
					  
					} catch (Exception e) {
						e.printStackTrace();
					}
				}		
			}
		});
		thread.start();
		
	}


}
