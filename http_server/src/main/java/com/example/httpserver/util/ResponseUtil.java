package com.example.httpserver.util;

import static io.netty.buffer.Unpooled.copiedBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WenHao
 * @ClassName ResponseUtil
 * @date 2022/7/22 17:24
 */
public class ResponseUtil {

  /**
   * response
   */
  private static FullHttpResponse response(ByteBuf content, HttpVersion version, HttpResponseStatus status, HashMap<CharSequence, Object> headers){
    FullHttpResponse response;
    if(null != content){
      response = new DefaultFullHttpResponse(version, status, content);
    }else{
      response = new DefaultFullHttpResponse(version, status);
    }
    if(null != headers && headers.size() > 0){
      for (Map.Entry<CharSequence, Object> entry : headers.entrySet()) {
        response.headers().set(entry.getKey(), entry.getValue());
      }
    }
    response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
    return response;
  }

  /**
   * HTTP/1.1
   */
  public static FullHttpResponse responseHTTP_1_1(ByteBuf content, HttpResponseStatus status, HashMap<CharSequence, Object> headers){
    return response(content, HttpVersion.HTTP_1_1, status, headers);
  }

  /**
   * application/json
   */
  public static FullHttpResponse responseJson(String json){
    ByteBuf content = copiedBuffer(json, CharsetUtil.UTF_8);
    HashMap<CharSequence, Object> headers = new HashMap<>(3);
    headers.put(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
    return responseHTTP_1_1(content, HttpResponseStatus.OK, headers);
  }

  /**
   * text/plain
   */
  public static FullHttpResponse responseText(HttpResponseStatus status, String msg) {
    ByteBuf content = copiedBuffer(msg, CharsetUtil.UTF_8);
    HashMap<CharSequence, Object> headers = new HashMap<>(3);
    headers.put(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
    return responseHTTP_1_1(content, status, headers);
  }

  /**
   * text/plain
   */
  public static FullHttpResponse responseText(String msg) {
    return responseText(HttpResponseStatus.OK, msg);
  }

  /**
   * icon
   */
  public static FullHttpResponse responseIco(ByteBuf content, boolean cache, String time){
    HashMap<CharSequence, Object> headers = new HashMap<>(16);
    if(cache){
      headers.put(HttpHeaderNames.DATE, time);
      return responseHTTP_1_1(null, HttpResponseStatus.NOT_MODIFIED, headers);
    }
    headers.put(HttpHeaderNames.CONTENT_TYPE, "image/x-icon");
    Date dd = new Date(System.currentTimeMillis());
    headers.put(HttpHeaderNames.DATE, dd.toString());
    headers.put(HttpHeaderNames.LAST_MODIFIED, dd.toString());
    headers.put(HttpHeaderNames.EXPIRES, new Date(System.currentTimeMillis()+(1000*60*60)).toString());
    headers.put(HttpHeaderNames.CACHE_CONTROL, "private, max-age=60");
    return responseHTTP_1_1(content, HttpResponseStatus.OK, headers);
  }

  /**
   * icon
   */
  public static FullHttpResponse responseIco(ByteBuf content){
    HashMap<CharSequence, Object> headers = new HashMap<>(3);
    headers.put(HttpHeaderNames.CONTENT_TYPE, "image/x-icon");
    return responseHTTP_1_1(content, HttpResponseStatus.OK, headers);
  }

  /**
   * all file
   */
  public static FullHttpResponse responseByFileName(ByteBuf content, String fileName){
    HashMap<CharSequence, Object> headers = new HashMap<>(16);
    FileUtil fileUtil = new FileUtil();
    String contentType = fileUtil.getContentType(StringUtil.getFileExtension(fileName));
    headers.put(HttpHeaderNames.CONTENT_TYPE, contentType);
    if(contentType.contains("video")){
      // accept-ranges
      headers.put(HttpHeaderNames.ACCEPT_RANGES, HttpHeaderValues.BYTES);
    }
    return responseHTTP_1_1(content, HttpResponseStatus.OK, headers);
  }

  /**
   * 302
   */
  public static FullHttpResponse response302(String uri) {
    HashMap<CharSequence, Object> headers = new HashMap<>(7);
    headers.put(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "x-requested-with,content-type");
    headers.put(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "POST,GET");
    headers.put(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
    // redirect uri settings
    headers.put(HttpHeaderNames.LOCATION, uri);
    // set redirect response code
    return responseHTTP_1_1(null, HttpResponseStatus.PERMANENT_REDIRECT, headers);
  }

}
