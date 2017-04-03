package util;

public class JsonResult {
   private String errMsg;
   public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
private String resultCode;
   private Object data;
public String getResultCode() {
	return resultCode;
}
@Override
public String toString() {
	return "JsonResult [resultCode=" + resultCode + ", data=" + data + "]";
}
public void setResultCode(String resultCode) {
	this.resultCode = resultCode;
}
public Object getData() {
	return data;
}
public void setData(Object data) {
	this.data = data;
}
}
