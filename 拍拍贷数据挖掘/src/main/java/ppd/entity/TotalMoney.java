package ppd.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TotalMoney {
  private double total;
  private String recordTime;
  private String table;
  private String startDate;

public String getStartDate() {
	return startDate;
}
public void setStartDate(String startDate) {
	this.startDate = startDate;
}
public String getTable() {
	return table;
}
public void setTable(String table) {
	this.table = table;
}
@Override
public String toString() {
	return "TotalMoney [total=" + total + ", recordTime=" + recordTime + "]";
}
public double getTotal() {
	return total;
}
public void setTotal(double total) {
	this.total = total;
}
public String getRecordTime() {
	return recordTime;
}
public void setRecordTime(String recordTime) {
	this.recordTime = recordTime;
}
}
