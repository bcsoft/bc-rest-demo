package cn.bc.demo;

import java.util.List;

/**
 * @author dragon 2016-07-14
 */
public interface DemoService {
	Demo get(Long id);

	List<Demo> findAll();

	Demo getByDao(Long id);
}