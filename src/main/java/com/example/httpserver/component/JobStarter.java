package com.example.httpserver.component;

import com.example.httpserver.bean.RunnerBean;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * @author WenHao
 * @ClassName JobStarter
 * @date 2022/7/18 17:47
 * @Description
 */
public class JobStarter {

  private LinkedList<RunnerBean> runnerBeans;

  public JobStarter(LinkedList<RunnerBean> runnerBeans) {
    this.runnerBeans = runnerBeans;
  }

  public void run(){
    Collections.sort(runnerBeans, new Comparator<RunnerBean>(){
      @Override
      public int compare(RunnerBean o1, RunnerBean o2) {
        return o1.getI() - o2.getI();
      }
    });
    for(RunnerBean runnerBean : runnerBeans){
      try{
        if(runnerBean.getM().getParameterTypes().length == 0){
          runnerBean.getM().invoke(runnerBean.getO());
        }else{
          runnerBean.getM().invoke(runnerBean.getO(), "");
        }
      }catch (ReflectiveOperationException e){
        e.printStackTrace();
      }
    }
  }

}
