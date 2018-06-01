package cn.bc.demo;

import java.util.Collection;

/**
 * Demo CRUD
 *
 * @author dragon 2016-07-14
 */
public interface DemoService {
  Demo load(Long id);

  Collection<Demo> find();

  Demo create(Demo demo);

  int update(Demo demo);

  int delete(Long id);

  int delete(Long[] ids);

  int deleteAll();
}