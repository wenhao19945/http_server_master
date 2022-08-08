package com.example.httpserver.util;

import java.net.URL;

/**
 * @author WenHao
 * @ClassName StringUtil
 * @date 2022/7/18 11:04
 * @Description
 */
public class StringUtil {

  private StringUtil() {

  }

  /**
   * /ddd.jpg  ->  .jpg
   */
  public static String getFileExtension(String fileName){
    return fileName.substring(fileName.lastIndexOf("."));
  }

  public static String[] getRequestPath(String uri){
    String[] str = uri.split("\\?");
    return str[0].replaceFirst("/","").split("/", 2);
  }

  public static boolean isBlank(String str) {
    if (str == null) {
      return true;
    }
    if (str.trim().length() < 1) {
      return true;
    }
    if ("".equals(str.trim())) {
      return true;
    }
    String nul = "null";
    if (nul.equals(str.trim().toLowerCase())) {
      return true;
    }
    return false;
  }

  /**
   * "file:/home/whf/cn/fh" -> "/home/whf/cn/fh"
   * "jar:file:/home/whf/foo.jar!cn/fh" -> "/home/whf/foo.jar"
   */
  public static String getRootPath(URL url) {
    String fileUrl = url.getFile();
    int pos = fileUrl.indexOf('!');

    if (-1 == pos) {
      return fileUrl;
    }

    return fileUrl.substring(5, pos);
  }

  /**
   * "cn.fh.lightning" -> "cn/fh/lightning"
   * @param name
   * @return
   */
  public static String dotToSplash(String name) {
    return name.replaceAll("\\.", "/");
  }

  /**
   * "Apple.class" -> "Apple"
   */
  public static String trimExtension(String name) {
    int pos = name.indexOf('.');
    if (-1 != pos) {
      return name.substring(0, pos);
    }

    return name;
  }

  /**
   * /application/home -> /home
   * @param uri
   * @return
   */
  public static String trimUri(String uri) {
    String trimmed = uri.substring(1);
    int splashIndex = trimmed.indexOf('/');

    return trimmed.substring(splashIndex);
  }

}
