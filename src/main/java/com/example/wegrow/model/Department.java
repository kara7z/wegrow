package com.example.wegrow.model;

/**
 * Department
 */
public class Department {
  private long id;
  private String name;
  private String description;
  private static long nextId = 0;

  Department() {
  }

  Department(String name, String description) {
    this.id = ++nextId;
    this.name = name;
    this.description = description;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setName(String name) {
    this.name = name;
  }

}
