package ppd.dao;

import ppd.entity.MyWebSign;

public interface MyWebSignDao {
	MyWebSign findByTime(MyWebSign myWebSign);
	void intsert(MyWebSign myWebSign);
}
