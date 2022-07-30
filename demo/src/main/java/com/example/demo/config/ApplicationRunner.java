package com.example.demo.config;

import com.example.httpserver.annotation.Component;
import com.example.httpserver.annotation.Runner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author WenHao
 * @ClassName Runner
 * @date 2022/7/18 19:29
 * @Description
 */
@Component
public class ApplicationRunner {

  private static final Logger logger = LoggerFactory.getLogger(
      ApplicationRunner.class);

  @Runner(index = 2)
  public void run2() {
    logger.info("run2");
  }

  @Runner
  public void run1() {
    logger.info("run1");
  }

  @Runner(index = 3)
  public void run3() throws Exception {
    logger.info("run3");
  }

}
