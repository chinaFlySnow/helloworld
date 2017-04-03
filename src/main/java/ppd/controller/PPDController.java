package ppd.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ppd.dao.TotalMoneyDao;
import ppd.entity.ResultCode;
import ppd.entity.TotalMoney;
import ppd.service.autoGetData.AutoInsertData;
import ppd.service.autoGetData.AutoRecordDataServlet;
import util.JsonResult;

@Controller
@RequestMapping("/PPD")
public class PPDController {
	
	private Logger logger = Logger.getLogger(PPDController.class);
	@Autowired
    private TotalMoneyDao totalMoneyDao;
	/**
	 * 获取拍拍贷今日重要数据
	 */
	@ResponseBody
	@RequestMapping("/getTodaysPPDImporData.do")
	public Object getTodaysPPDImporData(){
		
		logger.info("进入获取PPD今日重要数据方法===========》");
		JsonResult result = new JsonResult();
		result.setResultCode(ResultCode.SUCCESS);
		List<Map<String,TotalMoney>> list = new ArrayList<Map<String,TotalMoney>>();
		//获得今天的数据
		Map<String,TotalMoney> data1 = null;
		//获得昨天的数据，用来相减获得今日变化值
		Map<String,TotalMoney> data2 = null;
		
		TotalMoney totalMoney = new TotalMoney();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, -1);
        totalMoney.setRecordTime(sdf.format((new Date())));
        
	       try{
	    	   
	    	   data1 = getTodaysPPDImporDataFromOracle(totalMoney);
	    	   totalMoney.setRecordTime(sdf.format((cal.getTime())));
	    	   data2 = getTodaysPPDImporDataFromOracle(totalMoney);
	    	   list.add(data1);
	    	   list.add(data2);
	            }catch(Exception e){
	            	result.setResultCode(ResultCode.FALED);
	            	result.setErrMsg("失败信息："+e.getMessage());
	            	logger.info("获取PPD今日重要数据方法异常信息："+e.getMessage());
	            	e.printStackTrace();
	            }
		
        result.setData(list);
        return result;
	}
	
	@ResponseBody
	@RequestMapping("/getPictureData.do")
	public Object getPictureData(String dataSoure,String endDate,String startDate){
		
		logger.info("进入柱状图数据获取方法===========》");
		JsonResult result = new JsonResult();
		result.setResultCode(ResultCode.SUCCESS);
		List<TotalMoney> list=null;
		 
		try{
		Map<String,String> map = new HashMap<String,String>();
		map.put("dataSoure", dataSoure);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		list = totalMoneyDao.findByDate(map);
		
		}catch(Exception e){
			logger.info("获取柱状图数据失败，异常信息："+e.getMessage());
			result.setResultCode(ResultCode.FALED);
			result.setErrMsg(e.getMessage());
			e.printStackTrace();
		}
		
        result.setData(list);
        return result;
	}
	
	private Map<String,TotalMoney> getTodaysPPDImporDataFromOracle(TotalMoney totalMoney){
		Map<String,TotalMoney> data = new HashMap<String,TotalMoney>();
		
		  // 3.1获取账户总资产
	    totalMoney.setTable("totalMoney");
	    List<TotalMoney> list = totalMoneyDao.findTotalMoney(totalMoney);
        data.put("totalMoney", list.get(0));
        // 3.1获取账户总资产
	    totalMoney.setTable("totalprofile");
	    list = totalMoneyDao.findTotalMoney(totalMoney);
        data.put("totalprofile", list.get(0));
        //3.1.5获取总投入金额
      	totalMoney.setTable("totalInvestment");
        list = totalMoneyDao.findTotalMoney(totalMoney);
      	data.put("totalInvestment", list.get(0));
		//3.2获取坏账总数
		totalMoney.setTable("badTotalMoney");
		list = totalMoneyDao.findTotalMoney(totalMoney);
		data.put("badTotalMoney", list.get(0));
		//3.3获取坏账》30的数据
		totalMoney.setTable("badMoreThanThirty");
		list = totalMoneyDao.findTotalMoney(totalMoney);
		data.put("badMoreThanThirty", list.get(0));
		//3.3获取自己计算的累计利润
	    totalMoney.setTable("myTotalprofile");
		list = totalMoneyDao.findTotalMoney(totalMoney);
	    data.put("myTotalprofile", list.get(0));
		
		
		
		return data;
		
	}

}
