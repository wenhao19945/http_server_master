package com.example.httpserver.scanner;

import com.example.httpserver.ApplicationData;
import com.example.httpserver.annotation.Action;
import com.example.httpserver.annotation.Component;
import com.example.httpserver.annotation.Runner;
import com.example.httpserver.bean.RunnerBean;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author WenHao
 * @ClassName AnnotationScanner
 * @date 2022/7/18 14:41
 * @Description
 */
public class AnnotationScanner {

  private static final Logger logger = LoggerFactory.getLogger(AnnotationScanner.class);

  /**
   * Start annotation classification
   */
  public void begin(List<String> nameList){
    for (String className : nameList) {
      try{
        Class<?> clazz = Class.forName(className);
        Action action = clazz.getDeclaredAnnotation(Action.class);
        if(null != action){
          Map<String, String> map = new HashMap<>(16);
          map.put(action.value(), className);
          ApplicationData.ACTION.add(map);
        }
        Component component = clazz.getDeclaredAnnotation(Component.class);
        if(null != component){
          try{
            Map<String, Object> map = new HashMap<>(16);
            Object o = clazz.newInstance();
            map.put(component.name(), o);
            ApplicationData.COMPONENT.add(map);
            logger.info("Register Component : " + className);
            traversalMethod(o, clazz.getDeclaredMethods());
          }catch (Exception e){
            e.printStackTrace();
          }
        }
      }catch (ClassNotFoundException e){
        logger.error("Class not found : {}", className);
        e.printStackTrace();
      }
    }
  }

  /**
   * traversal method
   */
  private void traversalMethod(Object o, Method[] methods){
    for(Method method : methods){
      Runner runner = method.getDeclaredAnnotation(Runner.class);
      if(null != runner){
        RunnerBean runnerBean = new RunnerBean(runner.index(), o, method);
        ApplicationData.RUNNER.add(runnerBean);
      }
    }
  }

}
