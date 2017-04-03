package util;

import org.apache.log4j.Logger;

import commonSearch.controller.CommonSercherController;

public class CommonUtil {
	private static final Logger log = Logger.getLogger(CommonUtil.class);
	
    public static int findPageInputPosition( JsonResult jsonResult,String firstPageURL,String secondPageURL){
    	log.info("进入找出不同点位置方法==============》");
		//检查是否一样，一样代表输入有误
		if(firstPageURL.equals(secondPageURL)){
			jsonResult.setErrMsg("第一和第二页一样，输入有误");
			return -1;
		}
		//获取地址的char数组
		char[] firstPage=firstPageURL.toCharArray();
		char[] secondPage=secondPageURL.toCharArray();
		
		//长度对比，不一样长，则表示输入有误
		if(firstPage.length!=secondPage.length){
			jsonResult.setErrMsg("不一样长，输入有误");
			return -1;
		}
		
		//定义输入页面位置pagePosition
		int pagePosition=-1;
		
		//不同的位置有几处，超过或者小于1次都报错，提示输入有误
		int changTime=0;
		
		//循环数组找出pagePosition
		for(int i=0;i<firstPage.length;i++){
			
			if(firstPage[i]!=secondPage[i]){
				pagePosition=i;
				changTime++;
			}
		}
		
		//检测结果
		if(pagePosition==-1 || changTime==0 || changTime>1){
			jsonResult.setErrMsg("查找页面输入位置失败，结果如下："+"pagePosition:"+pagePosition+" changTime:"+changTime);
			return -1;
		}
		
		return pagePosition;
    }
}
