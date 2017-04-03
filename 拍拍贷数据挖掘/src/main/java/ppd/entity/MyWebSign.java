package ppd.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyWebSign {
    private String signDate;
    private String haveSign;

    
    public MyWebSign() {
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    	this.signDate = sdf.format(new Date());
    	haveSign="1";
    }
    
	public String getSignDate() {
		return signDate;
	}
	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}
	public String getHaveSign() {
		return haveSign;
	}
	public void setHaveSign(String haveSign) {
		this.haveSign = haveSign;
	}
    
}
