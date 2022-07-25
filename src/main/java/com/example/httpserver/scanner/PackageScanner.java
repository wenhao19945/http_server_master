package com.example.httpserver.scanner;

import java.io.IOException;
import java.util.List;

/**
 * @author WenHao
 * @ClassName PackageScanner
 * @date 2022/7/21 19:39
 * @Description
 */
public interface PackageScanner {

  /**
   * PackageScanner.getFullyQualifiedClassNameList
   * @throws IOException
   * @author WenHao
   * @date 2022/7/22 14:49
   * @return java.util.List<java.lang.String>
   */
  List<String> getFullyQualifiedClassNameList() throws IOException;

}
