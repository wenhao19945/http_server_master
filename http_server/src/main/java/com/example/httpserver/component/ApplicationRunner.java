package com.example.httpserver.component;

import com.example.httpserver.ApplicationData;
import com.example.httpserver.annotation.Component;
import com.example.httpserver.annotation.Runner;
import com.example.httpserver.bean.SettingsEnum;
import com.example.httpserver.db.MsqlBase;
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

  private static final Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);

  @Runner(index = 2)
  public void run2() {
    logger.info("run2");
  }

  @Runner
  public void run1() {
    MsqlBase db = new MsqlBase(
        ApplicationData.SETTINGS.get(SettingsEnum.mysql_url.toString()).toString(),
        ApplicationData.SETTINGS.get(SettingsEnum.mysql_user.toString()).toString(),
        ApplicationData.SETTINGS.get(SettingsEnum.mysql_pass.toString()).toString());
    if(db.init()){
      ApplicationData.DATA_BASE = db;
    }
  }

  @Runner(index = 3)
  public void run3() throws Exception {
    logger.info("run3");
  }

}
