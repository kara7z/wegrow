package com.example.wegrow.repository;

import com.example.wegrow.enums.ContractType;
import com.example.wegrow.model.Employee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.ListCrudRepository;

public interface EmployeeRepository extends ListCrudRepository<Employee, Long> {
  Optional<Employee> findByRegistrationNumber(String registrationNumber);

  List<Employee> findByDepartmentId(Long departmentId);

  long countByDepartmentId(Long departmentId);

  List<Employee> findByContractType(ContractType contractType);
}
