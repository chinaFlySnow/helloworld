package ppd.service.autoGetData;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import ppd.dao.MyWebSignDao;
import ppd.entity.MyWebSign;

@Component
public class MyWebSignService {
	
	static public MyWebSignDao myWebSignDao;
	private Logger logger = Logger.getLogger(MyWebSignService.class);
	@Resource
	public void setMyWebSignDao(MyWebSignDao myWebSignDao) {
		MyWebSignService.myWebSignDao = myWebSignDao;
	}
	
	public int insertMyWebSign(){
		
		logger.info("进入系统今日签到===========》");
		
		int insertSuccess = 1;
		
		try{
			
			myWebSignDao.intsert(new MyWebSign());
			logger.info("系统今日签到成功");
		}catch(Exception e){
			insertSuccess = 0;
			logger.info("系统签到失败信息"+e.getMessage());
			e.printStackTrace();
		}
		
		return insertSuccess;
		
	}
	
	public MyWebSign searchSignByTodayTime(){
		
		logger.info("检查今日系统是否签到===========》");
		
		MyWebSign myWebSign=null;
		
		try{
			
			myWebSign = myWebSignDao.findByTime(new MyWebSign());
			
		}catch(Exception e){
			logger.info("检查今日新帖是否签到失败信息"+e.getMessage()+"===========》");
			e.printStackTrace();
		}
		
		return myWebSign;
		
	}
	
	
}
