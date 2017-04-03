package commonSearch.entity;

public class SearchingResult {
   private String result;
   private String information;
public String getResult() {
	return result;
}
@Override
public String toString() {
	return "SearchingResult [result=" + result + ", information=" + information + "]";
}
public void setResult(String result) {
	this.result = result;
}
public String getInformation() {
	return information;
}
public void setInformation(String information) {
	this.information = information;
}
}
