package com.example.demo.pojo;

/**
 * @author WenHao
 * @ClassName User
 * @date 2022/7/22 14:58
 * @Description
 */
public class User {

  private Integer id;
  private String name;
  private String phone;
  private String createTime;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }
}
