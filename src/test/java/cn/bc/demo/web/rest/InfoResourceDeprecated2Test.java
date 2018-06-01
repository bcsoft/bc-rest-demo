package cn.bc.demo.web.rest;

import cn.bc.rest.test.ClassicalJerseySpringTest;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

/**
 * 仅用于演示旧的 ClassicalJerseySpringTest 如何使用
 * <p>使用经典 JerseyTest 子类的单元测试，不推荐使用，因为无法与 SpringJUnit4ClassRunner 配合使用）</p>
 */
@Deprecated
public class InfoResourceDeprecated2Test extends ClassicalJerseySpringTest {
  @Test
  public void resourceIsPrototype() {
    assertNotEquals(target("info/hash-code").request().get(String.class),
      target("info/hash-code").request().get(String.class));
  }
}