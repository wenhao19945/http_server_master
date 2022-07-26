package com.example.httpserver.db;

import com.alibaba.fastjson2.JSONObject;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author WenHao
 * @ClassName MsqlBase
 * @date 2022/7/22 10:46
 * @Description
 */
public class MsqlBase {
  private static final Logger logger = LoggerFactory.getLogger(MsqlBase.class);
  /**数据库驱动*/
  private String jdbcDriver;
  /**数据库url*/
  private String url;
  /**数据库名与密码*/
  private String user;
  private String pass;

  public MsqlBase(String url, String user, String pass) {
    this.jdbcDriver = "com.mysql.cj.jdbc.Driver";
    this.url = url;
    this.user = user;
    this.pass = pass;
  }

  public boolean init(){
    try{
      Class.forName(this.jdbcDriver);
      Connection conn = DriverManager.getConnection(this.url, this.user, this.pass);
      Statement stmt = conn.createStatement();
      String sql = "select 1";
      ResultSet rs = stmt.executeQuery(sql);
      rs.next();
      rs.close();
      stmt.close();
      conn.close();
      return true;
    }catch (Exception e){
      e.printStackTrace();
      return false;
    }
  }

  public <T> List<T> query(String sql, Class<T> t) {
    logger.info("query sql: {}", sql);
    List<T> list = new ArrayList<>();
    Field[] fields = t.getDeclaredFields();
    try{
      Connection conn = DriverManager.getConnection(this.url, this.user, this.pass);
      Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
      ResultSet rs = stmt.executeQuery(sql);
      while(rs.next()){
        Map<String, Object> map = new HashMap<>(16);
        for(Field field : fields){
          map.put(field.getName(), rs.getObject(field.getName()));
        }
        list.add(JSONObject.parseObject(JSONObject.toJSONString(map), t));
      }
      rs.close();
      stmt.close();
      conn.close();
    }catch (SQLException se){
      se.printStackTrace();
    }
    return list;
  }

}
