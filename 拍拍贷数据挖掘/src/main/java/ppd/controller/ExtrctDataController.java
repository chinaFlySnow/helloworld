package ppd.controller;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ppd.dao.TotalMoneyDao;
import ppd.entity.Profile;
import ppd.entity.TotalMoney;
import ppd.service.autoGetData.AutoInsertData;
import util.MyWebConnection;

@Controller
public class ExtrctDataController {

	@RequestMapping("/badmoneycaculation.do")
    @ResponseBody
	public double badMoneyCaculation() throws IOException {
		Document doc=MyWebConnection.getPaiPaiDaiPage("http://invest.ppdai.com/account/blacklist#list");
		Elements eles=doc.select(".pagerstatus");

		//如果只有一页将没有共几页这个按钮
		int totalPage=1;
		if(eles.size()!=0){
			String total=eles.get(0).text();
			totalPage=Integer.valueOf(total.substring(1, 2));
		}	
		//总额
		Profile profile=new Profile();
		//循环每一页的坏账
        for(int i=0;i<totalPage;i++){
        	String url="http://invest.ppdai.com/account/blacklist?PageIndex="+(i+1)+"&IsCalendarRequest=0";
        	badMoneyCaculationEveryPage(url,profile);
        }
        
		return profile.getProfile();
		
	}

	public Profile badMoneyCaculationEveryPage(String url,Profile profile) throws IOException {

	
			Document doc=MyWebConnection.getPaiPaiDaiPage(url);
			
			Elements eles=doc.select("tr:contains(%)");
			System.out.println(eles.size());
			
			//把小于30天的剔除
			if(AutoInsertData.needBigThanThirty){
			for(int z=0;z<eles.size();z++){
				Elements e=eles.get(z).select("td").eq(3);
				Element eee=e.get(0);
				String s=eee.text();
				String[] mon=s.split(" ");
				int day=Integer.valueOf(mon[0].trim());
				if(day<30){
					eles.remove(z);
					z--;
				}
			}
			}
			
			
			
			System.out.println("剔除以后还剩："+eles.size());
			
			double invesment=0;
			double back=0;
			//统计坏账
			for(Element ele:eles)
			{
				Elements e=ele.select("td").eq(1);
				Element eee=e.get(0);
				String s=eee.text();
				String[] mon=s.split("¥");
				
			//抓取还的多少和投了多少


			back+=Double.valueOf(mon[2].split(" ")[0].trim().replace(",", ""));
			System.out.println("收回："+back);


			invesment+=Double.valueOf(mon[3].trim().replace(",", ""));
			System.out.println("投资："+invesment);

					
					
				
				System.out.println();
			}
            
			profile.setProfile(profile.getProfile()+(invesment-back/1.1232));
			
			
			return profile;

	}
}
