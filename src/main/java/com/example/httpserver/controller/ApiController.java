package com.example.httpserver.controller;

import com.alibaba.fastjson2.JSON;
import com.example.httpserver.ApplicationData;
import com.example.httpserver.annotation.Action;
import com.example.httpserver.annotation.RequestApi;
import com.example.httpserver.bean.RequestMethod;
import com.example.httpserver.pojo.User;
import com.example.httpserver.util.ParameterUtil;
import com.example.httpserver.util.ResponseUtil;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author WenHao
 * @ClassName ApiController
 * @date 2022/7/15 17:36
 * @Description
 */
@Action("/index")
public class ApiController {

  private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

  @RequestApi(value = "/find", method = RequestMethod.GET)
  public FullHttpResponse find(FullHttpRequest request, FullHttpResponse response){
    Map<String, Object> param = ParameterUtil.getGetParamsFromChannel(request);
    int page = 1;
    String key = "page";
    if(param.containsKey(key)){
      page = Integer.parseInt(param.get(key).toString());
    }
    int start = (page-1) * 10;
    Map<String, Object> map = new HashMap<>(16);
    String sql = "select id, name, phone, create_time createTime from user limit " + start + ",10";
    List<User> list = ApplicationData.DATA_BASE.query(sql, User.class);
    map.put("code","200");
    map.put("msg","success");
    map.put("data",list);
    response = ResponseUtil.responseJson(JSON.toJSONString(map));
    return response;
  }

  @RequestApi(value = "/callback", method = RequestMethod.GET)
  public FullHttpResponse callback(FullHttpRequest request, FullHttpResponse response){
    logger.info("callback");
    String msg = "callback";
    response = ResponseUtil.responseText(msg);
    return response;
  }

  @RequestApi(value = "/list", method = RequestMethod.POST)
  public FullHttpResponse list(FullHttpRequest request, FullHttpResponse response){
    logger.info("login");
    String msg = "[{\"amount\":812000,\"createTime\":\"2022-07-15 09:19:07\",\"merchantId\":\"367152\"},{\"amount\":1925000,\"createTime\":\"2022-07-18 15:55:03\",\"merchantId\":\"482398\"}]";
    response = ResponseUtil.responseJson(msg);
    return response;
  }

}
