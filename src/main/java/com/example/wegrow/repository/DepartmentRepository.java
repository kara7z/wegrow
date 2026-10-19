package com.example.wegrow.repository;

import com.example.wegrow.model.Department;
import java.util.Optional;
import org.springframework.data.repository.ListCrudRepository;

public interface DepartmentRepository extends ListCrudRepository<Department, Long> {
  Optional<Department> findByName(String name);
}
