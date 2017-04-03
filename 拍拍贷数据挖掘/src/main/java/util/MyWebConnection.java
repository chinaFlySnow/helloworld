package util;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MyWebConnection {
	
	static Map<String,String> userCookies=null;
	
	/**
	 * 次方法用来获取参数页面的doc
	 * @param URL 需要获取的地址
	 * @return 回复doc
	 * @throws IOException
	 */
	public static Document getPaiPaiDaiPage(String URL) throws IOException{
		
		if(userCookies==null){
		
		P.print("登录页面，获取登录cookie");
			
		Map<String,String> loginPageCookies=Jsoup.connect("https://ac.ppdai.com/User/Login")
				.method(Connection.Method.POST).execute().cookies();
		
		//构造一个新cookie
		Connection.Response loginResponse = Jsoup.connect("https://ac.ppdai.com/User/Login")
				.data("mode", "login","UserName","youaccount","Password","youpassword", "skip", "1")
				.cookies(loginPageCookies)
				.method(Connection.Method.POST).execute();
		userCookies = loginResponse.cookies();
		
		}
		//使用cookie
		Document doc=Jsoup.connect(URL)
				.timeout(20000)
				.cookies(userCookies).get();
		
		return doc;
		
	}
	
	
}
