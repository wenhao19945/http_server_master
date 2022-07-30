package com.example.httpserver;

import com.example.httpserver.bean.RunnerBean;
import com.example.httpserver.db.MsqlBase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author WenHao
 * @ClassName ApplicationData
 * @date 2022/7/18 15:06
 * @Description application runtime data
 */
public class ApplicationData {

  /**
   * Action list.
   */
  public final static List<Map<String, String>> ACTION = new ArrayList<>();

  /**
   * Component list.
   */
  public final static List<Map<String, Object>> COMPONENT = new ArrayList<>();

  /**
   * Runner list.
   */
  public final static LinkedList<RunnerBean> RUNNER = new LinkedList<>();

  /**
   * global settings.
   */
  public final static Map<String, String> SETTINGS = new HashMap<>(16);

  /**
   * mysql db.
   */
  public static MsqlBase DATA_BASE = null;

}
