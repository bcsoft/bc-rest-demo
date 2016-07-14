package cn.bc.demo;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author dragon 2016-07-14
 */
@Singleton
@Named
class DemoDaoImpl implements DemoDao {
	private final static Map<Long, Demo> demos = new LinkedHashMap<>();
	private static long nextId = 0;

	@Override
	public Demo load(Long id) {
		return demos.get(id);
	}

	@Override
	public Collection<Demo> find() {
		return demos.values();
	}

	@Override
	public Demo create(Demo demo) {
		Long id = ++nextId;
		demo.setId(id);
		demos.put(id, demo);
		return demo;
	}

	@Override
	public int update(Demo demo) {
		Long id = demo.getId();
		if (demos.containsKey(id)) {
			demos.put(id, demo);
			return 1;
		} else return 0;
	}

	@Override
	public int delete(Long id) {
		if (demos.containsKey(id)) {
			demos.remove(id);
			return 1;
		}
		return 0;
	}

	@Override
	public int delete(Long[] ids) {
		if (ids == null) return 0;
		int c = 0;
		for (Long id : ids) {
			c += delete(id);
		}
		return c;
	}

	@Override
	public int deleteAll() {
		int c = demos.size();
		demos.clear();
		return c;
	}
}