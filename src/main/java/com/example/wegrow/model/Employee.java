package com.example.wegrow.model;

import com.example.wegrow.enums.ContractType;

/**
 * Employee
 */
public class Employee {

  private long id;
  private String registrationNumber;
  private String lastName;
  private String firstName;
  private String position;
  private ContractType contractType;
  private long departementId;
  private static long nexId = 0;

  Employee() {

  }

  Employee(String lastName, String firstName, String position,
      ContractType contractType, Long departementId) {
    this.id = ++nexId;
    this.registrationNumber = createRegistrationNumber();
    this.lastName = lastName;
    this.firstName = firstName;
    this.position = position;
    this.contractType = contractType;
    this.departementId = departementId;
  }

  public String createRegistrationNumber() {
    Long id = getId();

    if (id > 9 && id < 99) {
      return "EMP" + "0" + id;
    } else if (id > 0 && id < 9) {
      return "EMP" + "00" + id;
    } else {
      return "EMP" + id;
    }

  }

  public ContractType getContractType() {
    return contractType;
  }

  public String getFirstName() {
    return firstName;
  }

  public long getDepartementId() {
    return departementId;
  }

  public long getId() {
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

  public void setDepartementId(long departementId) {
    this.departementId = departementId;
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
}
