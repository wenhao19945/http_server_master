package com.example.httpserver.util;

import static io.netty.buffer.Unpooled.copiedBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * @author WenHao
 * @ClassName ResponseUtil
 * @date 2022/7/22 17:24
 * @Description
 */
public class ResponseUtil {

  /**
   * application/json
   */
  public static FullHttpResponse responseJson(String json){
    ByteBuf content = copiedBuffer(json, CharsetUtil.UTF_8);
    FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
    if (content != null) {
      response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
      response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
    }
    return response;
  }

  /**
   * text/plain
   */
  public static FullHttpResponse responseText(HttpResponseStatus status, String msg) {
    ByteBuf content = copiedBuffer(msg, CharsetUtil.UTF_8);
    FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);
    if (content != null) {
      response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
      response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
    }
    return response;
  }

  /**
   * text/plain
   */
  public static FullHttpResponse responseText(String msg) {
    ByteBuf content = copiedBuffer(msg, CharsetUtil.UTF_8);
    FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
    if (content != null) {
      response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
      response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
    }
    return response;
  }

  /**
   * icon
   */
  public static FullHttpResponse responseIco(ByteBuf content){
    FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
    if (content != null) {
      response.headers().set(HttpHeaderNames.CONTENT_TYPE, "image/x-icon");
      response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
    }
    return response;
  }

  /**
   * 302
   * @param uri
   * @author WenHao
   * @date 2022/7/22 17:25
   * @return io.netty.handler.codec.http.FullHttpResponse
   */
  public static FullHttpResponse response302(String uri) {
    //设置重定向响应码 （临时重定向、永久重定向）
    FullHttpResponse response = new DefaultFullHttpResponse(
        HttpVersion.HTTP_1_1, HttpResponseStatus.PERMANENT_REDIRECT);
    HttpHeaders headers = response.headers();
    headers.set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "x-requested-with,content-type");
    headers.set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "POST,GET");
    headers.set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
    headers.set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
    //重定向uri设置
    headers.set(HttpHeaderNames.LOCATION, uri);
    return response;
  }

}
