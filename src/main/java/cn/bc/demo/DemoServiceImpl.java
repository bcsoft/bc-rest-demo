package cn.bc.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dragon 2016-07-14
 */
@Named
@Singleton
public class DemoServiceImpl implements DemoService {
	private final static Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);
	@Inject
	private DemoDao dao;

	@Override
	public Demo get(Long id) {
		logger.debug("id={}", id);
		if (id > 0 && id < 11) return createDemo(id);
		else if (id == 11) return null;
		else throw new IllegalArgumentException("this id is not support. id=" + id);
	}

	@Override
	public Demo getByDao(Long id) {
		logger.debug("id={}", id);
		return dao.get(id);
	}

	@Override
	public List<Demo> findAll() {
		List<Long> ids = new ArrayList<>();
		for (int i = 1; i < 11; i++) ids.add(new Long(i));
		return ids.stream().map(this::createDemo).collect(Collectors.toList());
	}

	private Demo createDemo(Long id) {
		return new Demo() {{
			setId(id);
			setStr("test测试" + id);
			setBln(id % 2 == 0);
		}};
	}
}