package com.example.wegrow.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.example.wegrow.enums.ContractType;

/**
 * Employee
 */
@Table("employees")
public class Employee {

  @Id
  private Long id;

  private String registrationNumber;

  private String lastName;

  private String firstName;

  private String position;

  private ContractType contractType;

  private Long departmentId;

  private static long nextRegistrationSeq = 0;

  protected Employee() {
  }

  public Employee(String lastName, String firstName, String position,
      ContractType contractType, Long departmentId) {
    this.registrationNumber = createRegistrationNumber();
    this.lastName = lastName;
    this.firstName = firstName;
    this.position = position;
    this.contractType = contractType;
    this.departmentId = departmentId;
  }

  public String createRegistrationNumber() {
    long number = ++nextRegistrationSeq;

    if (number > 9 && number < 99) {
      return "EMP" + "0" + number;
    } else if (number > 0 && number < 9) {
      return "EMP" + "00" + number;
    } else {
      return "EMP" + number;
    }

  }

  public String getRegistrationNumber() {
    return registrationNumber;
  }

  public ContractType getContractType() {
    return contractType;
  }

  public String getFirstName() {
    return firstName;
  }

  public Long getDepartmentId() {
    return departmentId;
  }

  public Long getId() {
    return id;
  }

  public String getLastName() {
    return lastName;
  }

  public String getPosition() {
    return position;
  }

  public void setContractType(ContractType contractType) {
    this.contractType = contractType;
  }

  public void setDepartmentId(Long departmentId) {
    this.departmentId = departmentId;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public void setRegistrationNumber(String registrationNumber) {
    this.registrationNumber = registrationNumber;
  }
}
