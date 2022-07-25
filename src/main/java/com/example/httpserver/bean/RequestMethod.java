package com.example.httpserver.bean;

/**
 * RequestMethod.
 *
 * @author WenHao
 * @date 2022/7/22 14:50
 */
public enum RequestMethod {
  /**
   * request method type.
   */
  GET,
  HEAD,
  POST,
  PUT,
  PATCH,
  DELETE,
  OPTIONS,
  TRACE;

  private RequestMethod() {
  }
}
