package cn.bc.demo;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collection;

/**
 * @author dragon 2016-07-14
 */
@Singleton
@Named
public class DemoServiceImpl implements DemoService {
	@Inject
	private DemoDao dao;

	@Override
	public Demo load(Long id) {
		return dao.load(id);
	}

	@Override
	public Collection<Demo> find() {
		return dao.find();
	}

	@Override
	public Demo create(Demo demo) {
		return dao.create(demo);
	}

	@Override
	public int update(Demo demo) {
		return dao.update(demo);
	}

	@Override
	public int delete(Long id) {
		return dao.delete(id);
	}

	@Override
	public int delete(Long[] ids) {
		return dao.delete(ids);
	}

	@Override
	public int deleteAll() {
		return dao.deleteAll();
	}
}