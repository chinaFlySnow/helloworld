package ppd.service.autoGetData;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import ppd.controller.ExtrctDataController;
import ppd.dao.MyWebSignDao;
import ppd.dao.TotalMoneyDao;
import ppd.entity.TotalMoney;
import util.MyWebConnection;


@Component
public class AutoInsertData {
	//用来控制是否坏账要大于30天
	static public boolean needBigThanThirty=true;
	static public TotalMoneyDao totalMoneyDao;
	private Logger logger = Logger.getLogger(AutoInsertData.class);
	@Resource
	public void setTotalMoneyDao(TotalMoneyDao totalMoneyDao) {
		AutoInsertData.totalMoneyDao = totalMoneyDao;
	}
	
	public void  autoInsert() throws IOException{
		logger.info("进入每日8点自动插入重要数据==============》");
		//1.获取需要写入的数据
		Map<String,Double> needData = new HashMap<String,Double>();
		needData = getNeedData(needData);
		//2.构造数据entity
		TotalMoney totalMoney = new TotalMoney();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        totalMoney.setRecordTime(sdf.format(new Date()));
			
		//3.自动记录总额数据
        
        //3.0插入自己计算的累计利润
        try{
        totalMoney.setTable("myTotalprofile");
        double myTotalprofile = needData.get("totalMoney")/1.09-needData.get("badMoreThanThirty")-needData.get("totalInvestment");
	    totalMoney.setTotal(myTotalprofile);
        totalMoneyDao.intsert(totalMoney);
        }catch(Exception e){
        	logger.info("插入自己计算的利润失败，很可能是由于 计算总投入额时失败"+e.getMessage());
        	e.printStackTrace();
        }
        
	    //3.1插入 账户总资产
	    totalMoney.setTable("totalMoney");
	    totalMoney.setTotal(needData.get("totalMoney"));
        totalMoneyDao.intsert(totalMoney);
        
        //3.1插入 账户累计利润
	    totalMoney.setTable("totalprofile");
	    totalMoney.setTotal(needData.get("totalProfile"));
        totalMoneyDao.intsert(totalMoney);
		
		//3.2插入坏账总数
		totalMoney.setTable("badTotalMoney");
        totalMoney.setTotal(needData.get("badTotalMoney"));
		totalMoneyDao.intsert(totalMoney);
		
		//3.3插入坏账》30的数据
		totalMoney.setTable("badMoreThanThirty");
        totalMoney.setTotal(needData.get("badMoreThanThirty"));
		totalMoneyDao.intsert(totalMoney);
		
		//3.4插入总投资额度
		try{
	    totalMoney.setTable("totalInvestment");
	    totalMoney.setTotal(needData.get("totalInvestment"));
        totalMoneyDao.intsert(totalMoney);
		}catch(Exception e){
			logger.info("插入总额失败"+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	private Map<String,Double> getNeedData(Map<String,Double> needData) throws IOException{
		
		//获取 账户总资产
		Document doc=MyWebConnection.getPaiPaiDaiPage("http://invest.ppdai.com/account/lend");
		Elements eles=doc.select(".c666666.fr");
		String total=eles.get(0).text();
		Double totalPage=Double.valueOf(total.replace(",", "").substring(7));
		needData.put("totalMoney",totalPage);
		
		//获取总利润
		eles=doc.select(".ft26");
		String totalProfil = eles.get(1).text();
		Double totalProfile=Double.valueOf(totalProfil.replace(",", "").substring(1));
		needData.put("totalProfile",totalProfile);
		
		//获取 总投资额度
		try{
		   //1.获取截止昨天总投入金额
				TotalMoney totalMoney = new TotalMoney();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.DAY_OF_MONTH, -1);
		        totalMoney.setRecordTime(sdf.format((cal.getTime())));
		      	totalMoney.setTable("totalInvestment");
		      	double investMoney = totalMoneyDao.findTotalMoney(totalMoney).get(0).getTotal();
		        System.out.println("今日之前总共投入"+investMoney);
		        
		        //2.加上昨日投入的资金
				doc=MyWebConnection.getPaiPaiDaiPage("http://www.ppdai.com/moneyhistory?Type=0&Time=7");
				//获得每条充值记录
				eles=doc.select("tr");
				eles.remove(0);
				for(Element ele:eles){
					//获取每条的td
					Elements subEles=ele.select("td");
					//获取充值时间
					String date = subEles.get(0).text().split(" ")[0];
					SimpleDateFormat sd2f = new SimpleDateFormat("yyyy/M/dd");
					Calendar cal2 = Calendar.getInstance();
					cal2.setTime(new Date());
					cal2.add(Calendar.DAY_OF_MONTH, -1);
					String yestoday = sd2f.format(cal2.getTime());
					
					//取出昨天的充值数据
					System.out.println(yestoday+":"+date);
					if(yestoday.equals(date)){
						String money = subEles.get(3).text().substring(1);
						investMoney = investMoney+Double.parseDouble(money);
						System.out.println("昨天投入:"+money+"截止现在总共投入："+investMoney);
					}
					
		            
					
				}
				
				//3.减去昨日提现的资金
				doc=MyWebConnection.getPaiPaiDaiPage("http://www.ppdai.com/moneyhistory?Type=2&Time=7");
				//获得每条充值记录
				eles=doc.select("tr");
				eles.remove(0);
				for(Element ele:eles){
					//获取每条的td
					Elements subEles=ele.select("td");
					//获取充值时间
					String date = subEles.get(0).text().split(" ")[0];
					SimpleDateFormat sd2f = new SimpleDateFormat("yyyy/M/dd");
					Calendar cal2 = Calendar.getInstance();
					cal2.setTime(new Date());
					cal2.add(Calendar.DAY_OF_MONTH, -1);
					String yestoday = sd2f.format(cal2.getTime());
					
					//取出昨天的充值数据
					if(yestoday.equals(date)){
						String money = subEles.get(2).text().substring(1);
						investMoney = investMoney-Double.parseDouble(money.replace(",", ""));
						System.out.println("昨天取现:"+money+"截止现在总共投入："+investMoney);
					}
					
		            
					
				}

		needData.put("totalInvestment",investMoney);		
		}catch(Exception e){
			logger.info("获取总投资额失败"+e.getMessage());
			e.printStackTrace();
		}
			
		//获取>30天坏账
		ExtrctDataController ed = new ExtrctDataController();
		Double badMoreThanThirty = ed.badMoneyCaculation();
		needData.put("badMoreThanThirty",badMoreThanThirty);
		
		//获取所有坏账
		needBigThanThirty=false;
		Double badTotalMoney = ed.badMoneyCaculation();
		needData.put("badTotalMoney",badTotalMoney);
		needBigThanThirty=true;
		
		return needData;
		
	}

}
