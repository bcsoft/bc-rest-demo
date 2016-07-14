package cn.bc.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author dragon 2016-07-14
 */
@Named
@Singleton
class DemoDaoImpl implements DemoDao {
	private final static Logger logger = LoggerFactory.getLogger(DemoDaoImpl.class);

	@Override
	public Demo get(Long id) {
		logger.debug("id={}", id);
		return new Demo() {{
			setId(id);
			setStr("test测试" + id);
			setBln(id % 2 == 0);
		}};
	}
}