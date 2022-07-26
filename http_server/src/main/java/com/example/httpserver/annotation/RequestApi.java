package com.example.httpserver.annotation;

import com.example.httpserver.bean.RequestMethod;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author WenHao
 * @ClassName RequestApi
 * @date 2022/7/19 11:48
 * @Description
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestApi {

  String value();

  RequestMethod method();

}
