package com.example.httpserver.scanner;

import com.example.httpserver.util.StringUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author WenHao
 * @ClassName ClasspathPackageScanner
 * @date 2022/7/15 17:51
 * @Description
 * 思路：
 * 有的web server在部署运行时会解压jar包，因此class文件会在普通的文件目录下。
 * 如果web server不解压jar包，则class文件会直接存在于Jar包中。
 * 对于前者，只需定位到class文件所在目录，然后将class文件名读取出即可；
 * 对于后者，则需先定位到jar包所在目录，然后使用JarInputStream读取Jar包，得到class类名。
 */
public class ClasspathPackageScanner implements PackageScanner{

  private static final Logger logger = LoggerFactory.getLogger(ClasspathPackageScanner.class);

  private String basePackage;

  private ClassLoader cl;

  public ClasspathPackageScanner(String basePackage) {
    this.basePackage = basePackage;
    this.cl = getClass().getClassLoader();
  }

  @Override
  public List<String> getFullyQualifiedClassNameList() throws IOException {
    return doScan(basePackage, new ArrayList<>());
  }

  private List<String> doScan(String basePackage, List<String> nameList) throws IOException {
    String splashPath = StringUtil.dotToSplash(basePackage);
    URL url = cl.getResource(splashPath);
    String filePath = StringUtil.getRootPath(url);
    List<String> names = null;
    if (isJarFile(filePath)) {
      // jar file
      logger.info("{} this is a jar file ", filePath);

      names = readFromJarFile(filePath, splashPath);
    } else {
      // directory
      logger.info("{} this is a directory", filePath);

      names = readFromDirectory(filePath);
    }
    for (String name : names) {
      if (isClassFile(name)) {
        nameList.add(toFullyQualifiedName(name, basePackage, splashPath));
      } else {
        // this is a directory
        // check this directory for more classes
        // do recursive invocation
        doScan(basePackage + "." + name, nameList);
      }
    }
    for (String n : nameList) {
      logger.info("find : {}", n);
    }
    return nameList;
  }

  /**
   * Convert short class name to fully qualified name.
   */
  private String toFullyQualifiedName(String shortName, String basePackage, String splashPath) {
    int pos = shortName.indexOf(splashPath);
    if(-1 != pos){
      shortName = shortName.substring(pos).replaceAll("/", ".");
      return shortName.replace(".class", "");
    }else{
      StringBuilder sb = new StringBuilder(basePackage);
      sb.append('.');
      sb.append(StringUtil.trimExtension(shortName));
      return sb.toString();
    }
  }

  private boolean isJarFile(String name) {
    return name.endsWith(".jar");
  }

  private boolean isClassFile(String name) {
    return name.endsWith(".class");
  }

  /**
   * read file from jar file
   * 根据打包方式不一样，jar包内部结构也会有所不同，需要调整对应扫描方式
   */
  private List<String> readFromJarFile(String jarPath, String splashedPackageName) throws IOException {
    logger.info("read file from jar file -> {}", jarPath);

    JarInputStream jarIn = new JarInputStream(new FileInputStream(jarPath));
    JarEntry entry = jarIn.getNextJarEntry();

    List<String> nameList = new ArrayList<>();
    while (null != entry) {
      String name = entry.getName();
      if (name.contains(splashedPackageName) && isClassFile(name)) {
        logger.info("jar file item -> {}", name);
        nameList.add(name);
      }
      entry = jarIn.getNextJarEntry();
    }

    return nameList;
  }

  /**
   * Read file from directory
   */
  private List<String> readFromDirectory(String path) {
    logger.info("read file from directory -> {}", path);
    File file = new File(path);
    String[] names = file.list();
    if (null == names) {
      return null;
    }
    return Arrays.asList(names);
  }

}
