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

  public void load() throws Exception{
    //路径
    BufferedReader reader = null;
    StringBuffer str = new StringBuffer();
    try {
      InputStream fileInputStream = this.getClass().getClassLoader().getResourceAsStream("settings.json");
      InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
      reader = new BufferedReader(inputStreamReader);
      String tempString = null;
      while ((tempString = reader.readLine()) != null) {
        str.append(tempString);
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    try{
      //json
      JSONObject jsonData = JSONObject.parseObject(str.toString());
      for(String key : jsonData.keySet()){
        ApplicationData.SETTINGS.put(key, jsonData.get(key));
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }

}
