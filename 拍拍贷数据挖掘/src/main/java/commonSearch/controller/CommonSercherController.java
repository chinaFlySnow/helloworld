package commonSearch.controller;


import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.coyote.http11.upgrade.servlet31.WebConnection;
import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import commonSearch.entity.SearchingResult;
import ppd.entity.ResultCode;
import util.CommonUtil;
import util.JsonResult;
import util.MyWebConnection;

@Controller
public class CommonSercherController {
	private final Logger log = Logger.getLogger(CommonSercherController.class);
	
	/**
	 * 获取URL变动页面规则和总页数
	 * @param firstPageURL
	 * @param secondPageURL
	 * @return
	 */
	@RequestMapping("/getPagePosition.do")
	@ResponseBody
	public JsonResult getPagePosition(String firstPageURL,String secondPageURL){
    	System.out.println(secondPageURL);
		log.info("进入获取URL位置总页数方法============》");
        JsonResult jsonResult = new JsonResult();
        jsonResult.setResultCode(ResultCode.SUCCESS);
        Map map = new HashMap<>();
		try {
			//获取页面输入位置
            int pagePosition=CommonUtil.findPageInputPosition(jsonResult,firstPageURL, secondPageURL);
            if(pagePosition==-1){
            	jsonResult.setResultCode(ResultCode.FALED);
            	return jsonResult;
            }
            map.put("pagePosition", pagePosition);
            //改变输入页面位置数字,来遍历所有页面
            //获取第一页页面大小
			String url = firstPageURL.substring(0,pagePosition)+"1"+firstPageURL.substring(pagePosition+1);
			Document doc = Jsoup.connect(url).get();

			
			//选出总共几页
			Elements eless = doc.select(".td");
//			int totalPage = Integer.parseInt(eless.get(0).text().split("页")[0].substring(1));
//			map.put("totalPage", totalPage);

			//Document dc=Jsoup.connect(firstPageURL).get();
			//System.out.println(dc);
		} catch (Exception e) {
			jsonResult.setResultCode(ResultCode.FALED);
			jsonResult.setErrMsg("获取URL位置总页数方法"+e.getMessage());
			log.info("获取URL位置总页数方法"+e.getMessage());
			e.printStackTrace();
		}
		jsonResult.setData(map);
		return jsonResult;
		
	}
	
	/**
	 * 搜索符合要求页面插入数据库
	 * @param firstPageURL
	 * @param secondPageURL
	 * @return
	 */
	@RequestMapping("/commonSercher.do")
	@ResponseBody
	public JsonResult commonSercher(String firstPageURL,String secondPageURL){
    	System.out.println(secondPageURL);
		log.info("进入通用查找方法============》");
        JsonResult jsonResult = new JsonResult();
        jsonResult.setResultCode(ResultCode.SUCCESS);
        Map map = new HashMap<>();
		try {
			//获取页面输入位置
            int pagePosition=CommonUtil.findPageInputPosition(jsonResult,firstPageURL, secondPageURL);
            if(pagePosition==-1){
            	jsonResult.setResultCode(ResultCode.FALED);
            	return jsonResult;
            }
            map.put("pagePosition", pagePosition);
            //改变输入页面位置数字,来遍历所有页面
            //获取第一页页面大小
			String url = firstPageURL.substring(0,pagePosition)+"1"+firstPageURL.substring(pagePosition+1);
			Document doc = Jsoup.connect(url).get();

			
			//选出总共几页
			Elements eless = doc.select(".td");
			int totalPage = Integer.parseInt(eless.get(0).text().split("页")[0].substring(1));
			map.put("totalPage", totalPage);
			
			//遍历totalPage页
			System.out.println("搜索开始============================================================》");
			for(int i = 0;i<totalPage;i++){
				Elements as = null;
				
				try{
				System.out.println("搜索第："+(i+1)+"页");
				url = firstPageURL.substring(0,pagePosition)+(i+1)+firstPageURL.substring(pagePosition+1);
				doc = Jsoup.connect(url).get();
				//选出每个招聘div
				Elements eles = doc.select(".t1");
				eles.remove(0);
				//选出div下的A标签的值
				as = eles.select("a");
				}catch(Exception e){
				    System.out.println("搜索第:"+(i+1)+"页是出错"+"出错链接为："+url);
					Thread.sleep(5000);
					continue;                                                           
				}
				
				//遍历页面里的每个A标签地址
                for(int j=0;j<as.size();j++){
                	try{
                	String subUrl = as.get(j).attr("href");
                	Document docc = Jsoup.connect(subUrl).get();
                	Elements els = docc.select(".bmsg");
                	String recalInformation = els.get(0).text();
                	if(recalInformation.contains("英语")){
                		System.out.print("这条可以符合要求，链接：");
                		System.out.println(subUrl);
                	}
                	Thread.sleep(500);
                	}catch(Exception e){
                		System.out.println("搜索第:"+(i+1)+"页第"+(j+1)+"个时出错"+"出错链接为："+url);
                		Thread.sleep(5000);
                		continue;
                	}
                }
				
				
			}

			System.out.println("搜索结束============================================================》");
			//Document dc=Jsoup.connect(firstPageURL).get();
			//System.out.println(dc);
		} catch (Exception e) {
			jsonResult.setResultCode(ResultCode.FALED);
			jsonResult.setErrMsg("通用查找方法异常:可能输入的地址有错"+e.getMessage());
			log.info("通用查找方法异常:可能输入的地址有错");
			e.printStackTrace();
		}
		jsonResult.setData(map);
		return jsonResult;
		
	}
	
    /**
     * 到处RUL的text文件到客户端
     */
	@RequestMapping("/getTextFile.do")
	public void exportTextFileOfHtml(String firstPageURL,HttpServletResponse response,String IsPPD){
	  log.info("进入导出text文件方法===================》");
	  BufferedOutputStream buff = null; 
	  StringBuffer write = new StringBuffer();   
      try {
    	  //获取网页text数据
    	  Document doc=null;
    	  try{
    	  if("true".equals(IsPPD)){
    		  doc = MyWebConnection.getPaiPaiDaiPage(firstPageURL);
    		  write.append(doc.toString());  
    	  }else{
    		  doc = Jsoup.connect(firstPageURL).get();
    		  write.append(doc.toString());  
    	  }
    	  }catch(Exception e){
    		  write.append("下载失败请查看URL是否正确！");  
    	  }
    	   

		  //导出txt文件 
		  response.setContentType("text/plain");  
		  String fileName="websiteTextFile"; 
		  try { 
		   fileName = URLEncoder.encode("网页的text文件", "UTF-8"); 
		  } catch (Exception e1) { 
		   e1.printStackTrace(); 
		  }  
		  response.setHeader("Content-Disposition","attachment; filename=" + fileName + ".txt");    
		  String enter = "\r\n";   
		  ServletOutputStream outSTr = null;   
		  try {   
		    outSTr = response.getOutputStream(); // 建立   
		    buff = new BufferedOutputStream(outSTr); 
		    //把内容写入文件  
		    buff.write(write.toString().getBytes("UTF-8"));   
		    buff.flush();   
		    buff.close();   
		  } catch (Exception e) {   
		   e.printStackTrace();   
		  } finally {   
		   try {   
		    buff.close();   
		    outSTr.close();   
		   } catch (Exception e) {   
		    e.printStackTrace();   
		   }   
		  } 


	  } catch (Exception e) {
		log.info("导出text文件方法失败信息："+e.getMessage());
		e.printStackTrace();  
		    
	  }
	}
	
	
		  /**
	     * 显示元素内容
	     */
		@RequestMapping("/showElementContent.do")
		@ResponseBody
		public Object showElementContent(String firstPageURL,String elementCharacter){
		  log.info("进入显示元素成分方法===================》");
		  JsonResult result = new JsonResult();
		  result.setResultCode(ResultCode.SUCCESS);
		  
		try {
			Document doc = Jsoup.connect(firstPageURL).get();
			
			
		} catch (IOException e) {
			result.setResultCode(ResultCode.FALED);
			result.setErrMsg(e.getMessage());
			log.info("显示元素成分方法失败信息："+e.getLocalizedMessage());
			e.printStackTrace();
		}
		  
		  
		  return elementCharacter;

		}
	
	

}
