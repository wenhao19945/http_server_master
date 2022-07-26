package com.example.httpserver;

import com.example.httpserver.component.JobStarter;
import com.example.httpserver.util.SettingUtil;
import com.example.httpserver.scanner.AnnotationScanner;
import com.example.httpserver.scanner.ClasspathPackageScanner;
import com.example.httpserver.server.NettyHttpServer;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author WenHao
 * @ClassName Application
 * @date 2022/7/15 18:07
 * @Description
 */
public class Application {

  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  private String basePackage;

  private Class<?> primarySources;

  private Application(Class<?> primarySources) {
    notNull(primarySources, "PrimarySources must not be null");
    this.primarySources = primarySources;
  }

  public static void run(Class<?> primarySource, String... args) {
    try {
      new Application(primarySource).run(args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void notNull(Object object, String message) {
    if (object == null) {
      throw new IllegalArgumentException(message);
    }
  }

  private void run(String... args) throws Exception {

    //com.example.httpserver.Start  -> com.example.httpserver
    this.basePackage = this.primarySources.getName().substring(0, this.primarySources.getName().lastIndexOf("."));
    logger.info("this base package:"+this.basePackage);
    //find all class for base package
    List<String> nameList = new ClasspathPackageScanner(this.basePackage).getFullyQualifiedClassNameList();
    logger.info("class list size:"+nameList.size());
    //find annotation
    if (null != nameList && nameList.size() > 0) {
      new AnnotationScanner().begin(nameList);
    }

    //load setting
    new SettingUtil().load();

    //start web
    httpServerStart();

    logger.info("Service started successfully !");

    //run other job ...
    if (ApplicationData.RUNNER.size() > 0) {
      JobStarter starter = new JobStarter(ApplicationData.RUNNER);
      starter.run();
    }

  }

  private void httpServerStart() {
    String keyName = "port";
    int port = 8080;
    if (ApplicationData.SETTINGS.containsKey(keyName)) {
      port = Integer.parseInt(ApplicationData.SETTINGS.get(keyName).toString());
    }
    new NettyHttpServer(port).start();
  }

}
