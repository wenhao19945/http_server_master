package com.example.httpserver.util;

import java.io.InputStream;

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

}
