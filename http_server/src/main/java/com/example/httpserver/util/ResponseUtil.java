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
   * all file
   */
  public static FullHttpResponse responseByFileName(ByteBuf content, String fileName){
    FileUtil fileUtil = new FileUtil();
    String contentType = fileUtil.getContentType(StringUtil.getFileExtension(fileName));
    FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
    if (content != null) {
      response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);
      response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
    }
    return response;
  }

  /**
   * 302
   */
  public static FullHttpResponse response302(String uri) {
    // set redirect response code
    FullHttpResponse response = new DefaultFullHttpResponse(
        HttpVersion.HTTP_1_1, HttpResponseStatus.PERMANENT_REDIRECT);
    HttpHeaders headers = response.headers();
    headers.set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "x-requested-with,content-type");
    headers.set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "POST,GET");
    headers.set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
    headers.set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
    // redirect uri settings
    headers.set(HttpHeaderNames.LOCATION, uri);
    return response;
  }

}
