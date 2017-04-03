package ppd.dao;

import java.util.List;
import java.util.Map;

import ppd.entity.TotalMoney;

public interface TotalMoneyDao {
	List<TotalMoney> findTotalMoney(TotalMoney totalMoney);
	void intsert(TotalMoney totalMoney);
	List<TotalMoney> findByDate(Map<String,String> totalMoney);
	
}
