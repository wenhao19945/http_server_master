package com.example.httpserver.util;

import com.alibaba.fastjson2.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author WenHao
 * @ClassName FileUtil
 * @date 2022/7/19 17:17
 * @Description
 */
public class FileUtil {

  /**
   * small file reading
   *
   * @param fileName
   * @author WenHao
   * @date 2022/7/19 19:32
   * @return byte[]
   */
  public byte[] loadIco(String fileName) throws Exception {
    InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
    byte[] ico = new byte[inputStream.available()];
    inputStream.read(ico, 0, ico.length);
    inputStream.close();
    return ico;
  }

  /**
   * FileUtil.loadFile.
   *
   * @param file
   * @author WenHao
   * @date 2022/8/9 17:09
   * @return byte[]
   */
  public static byte[] loadFile(File file) throws IOException{
    FileInputStream inputStream = new FileInputStream(file);
    byte[] fileBytes = new byte[inputStream.available()];
    inputStream.read(fileBytes, 0, fileBytes.length);
    inputStream.close();
    return fileBytes;
  }

  /**
   * FileUtil.getContentType
   *
   * @param extension
   * @author WenHao
   * @date 2022/8/8 16:28
   * @return java.lang.String
   */
  public String getContentType(String extension){
    String fileName = "content-type.json";
    InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
    String str = getString(inputStream, "UTF-8");
    JSONObject jsonData = JSONObject.parseObject(str);
    return jsonData.getString(extension);
  }

  /**
   * FileUtil.getString
   *
   * @param inputStream
   * @param charsetName
   * @author WenHao
   * @date 2022/8/8 16:24
   * @return java.lang.String
   */
  public static String getString(InputStream inputStream, String charsetName){
    //路径
    BufferedReader reader = null;
    StringBuffer str = new StringBuffer();
    try {
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charsetName);
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
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return str.toString();
  }

}
