package com.example.httpserver.bean;

import java.lang.reflect.Method;

/**
 * @author WenHao
 * @ClassName RunnerBean
 * @date 2022/7/18 17:30
 * @Description
 */
public class RunnerBean {

  int i;

  Object o;

  Method m;

  public RunnerBean(int i, Object o, Method m) {
    this.i = i;
    this.o = o;
    this.m = m;
  }

  public int getI() {
    return i;
  }

  public void setI(int i) {
    this.i = i;
  }

  public Object getO() {
    return o;
  }

  public void setO(Object o) {
    this.o = o;
  }

  public Method getM() {
    return m;
  }

  public void setM(Method m) {
    this.m = m;
  }

}
