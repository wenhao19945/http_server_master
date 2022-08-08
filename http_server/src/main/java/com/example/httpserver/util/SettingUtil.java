package com.example.httpserver.util;

import com.alibaba.fastjson2.JSONObject;
import com.example.httpserver.ApplicationData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author WenHao
 * @ClassName SettingUtil
 * @date 2022/7/19 11:16
 * @Description
 */
public class SettingUtil {

  /**
   * load settings.json file
   */
  public void load() {
    try{
      InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("settings.json");
      String str = FileUtil.getString(inputStream, "UTF-8");
      //json
      JSONObject jsonData = JSONObject.parseObject(str);
      for(String key : jsonData.keySet()){
        ApplicationData.SETTINGS.put(key, jsonData.get(key).toString());
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }

}
