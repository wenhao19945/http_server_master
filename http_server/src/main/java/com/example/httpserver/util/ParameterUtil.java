package com.example.httpserver.util;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ParameterUtil.
 * @author WenHao
 * @date 2022/7/22 17:06
 */
public class ParameterUtil {

  /**
   * get params
   */
  public static Map<String, Object> getParam(FullHttpRequest request) {
    if(request.method().equals(HttpMethod.GET)){
      return getGetParamsFromChannel(request);
    }else{
      return getPostParamsFromChannel(request);
    }
  }

  /**
   * get params for string
   */
  public static String getParamString(FullHttpRequest fullHttpRequest) {
    ByteBuf content = fullHttpRequest.content();
    byte[] reqContent = new byte[content.readableBytes()];
    content.readBytes(reqContent);
    String strContent = "";
    try {
      strContent = new String(reqContent, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return strContent;
  }

  /**
   * get the parameters passed by GET method.
   */
  public static Map<String, Object> getGetParamsFromChannel(FullHttpRequest fullHttpRequest) {
    Map<String, Object> params = new HashMap<>(16);
    QueryStringDecoder decoder = new QueryStringDecoder(fullHttpRequest.uri());
    Map<String, List<String>> paramList = decoder.parameters();
    for (Map.Entry<String, List<String>> entry : paramList.entrySet()) {
      params.put(entry.getKey(), entry.getValue().get(0));
    }
    return params;
  }

  /**
   * get the parameters passed by POST method.
   */
  public static Map<String, Object> getPostParamsFromChannel(FullHttpRequest fullHttpRequest) {
    Map<String, Object> params = null;
    if (fullHttpRequest.method() == HttpMethod.POST) {
      // 处理POST请求
      String strContentType = fullHttpRequest.headers().get(HttpHeaderNames.CONTENT_TYPE).trim();
      if (strContentType.contains(HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED)) {
        params  = getFormParams(fullHttpRequest);
      } else if (strContentType.contains(HttpHeaderValues.APPLICATION_JSON)) {
        try {
          params = getJsonParams(fullHttpRequest);
        } catch (UnsupportedEncodingException e) {
          return null;
        }
      } else {
        return null;
      }
      return params;
    } else {
      return null;
    }
  }

  /**
   * parse form data (Content-Type = application/x-www-form-urlencoded)
   */
  public static Map<String, Object> getFormParams(FullHttpRequest fullHttpRequest) {
    Map<String, Object> params = new HashMap<>(16);
    HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), fullHttpRequest);
    List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
    for (InterfaceHttpData data : postData) {
      if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
        MemoryAttribute attribute = (MemoryAttribute) data;
        params.put(attribute.getName(), attribute.getValue());
      }
    }
    return params;
  }

  /**
   * parse json data (Content-Type = application/json)
   */
  public static Map<String, Object> getJsonParams(FullHttpRequest fullHttpRequest) throws UnsupportedEncodingException {
    Map<String, Object> params = new HashMap<>(16);
    ByteBuf content = fullHttpRequest.content();
    byte[] reqContent = new byte[content.readableBytes()];
    content.readBytes(reqContent);
    String strContent = new String(reqContent, "UTF-8");
    JSONObject jsonParams = JSONObject.parseObject(strContent);
    for (Object key : jsonParams.keySet()) {
      params.put(key.toString(), jsonParams.get(key));
    }
    return params;
  }

}
