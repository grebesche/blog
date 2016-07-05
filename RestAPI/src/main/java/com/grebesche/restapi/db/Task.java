package com.grebesche.restapi.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Task {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Date start;

  @Column(nullable = false)
  private Date finish;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public Date getFinish() {
    return finish;
  }

  public void setFinish(Date finish) {
    this.finish = finish;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Task task = (Task) o;

    if (!id.equals(task.id)) return false;
    if (!name.equals(task.name)) return false;
    if (!start.equals(task.start)) return false;
    return finish.equals(task.finish);

  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + start.hashCode();
    result = 31 * result + finish.hashCode();
    return result;
  }
}
