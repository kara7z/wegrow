package com.example.wegrow.controller;

import com.example.wegrow.model.Department;
import com.example.wegrow.repository.DepartmentRepository;
import com.example.wegrow.repository.EmployeeRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DepartmentController
 */
@RestController
@RequestMapping("/api/departements")
public class DepartmentController {

  private final DepartmentRepository departments;
  private final EmployeeRepository employees;

  public DepartmentController(DepartmentRepository departments, EmployeeRepository employees) {
    this.departments = departments;
    this.employees = employees;
  }

  public record DeptRequest(String nom, String description) {
  }

  public record DeptResponse(Long id, String nom, String description) {
    static DeptResponse from(Department d) {
      return new DeptResponse(d.getId(), d.getName(), d.getDescription());
    }
  }

  public record DeptWithCountResponse(Long id, String nom, String description, Long nombreEmployes) {
    static DeptWithCountResponse from(Department d, Long count) {
      return new DeptWithCountResponse(d.getId(), d.getName(), d.getDescription(), count);
    }
  }

  @GetMapping
  public ResponseEntity<List<DeptWithCountResponse>> list() {
    List<DeptWithCountResponse> result = departments.findAll().stream()
        .map(d -> DeptWithCountResponse.from(d, employees.countByDepartmentId(d.getId())))
        .sorted(Comparator.comparing(DeptWithCountResponse::nom, String.CASE_INSENSITIVE_ORDER))
        .toList();
    return ResponseEntity.ok(result);
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody DeptRequest request) {
    if (request.nom() == null || request.nom().isBlank()) {
      return ResponseEntity.badRequest()
          .body(Map.of("message", "Le champ 'nom' est obligatoire"));
    }

    if (departments.findByName(request.nom()).isPresent()) {
      return ResponseEntity.status(409)
          .body(Map.of("message", "Un département avec ce nom existe déjà"));
    }

    try {
      Department saved = departments.save(new Department(request.nom(), request.description()));
      return ResponseEntity.status(201).body(DeptResponse.from(saved));
    } catch (DuplicateKeyException e) {
      return ResponseEntity.status(409)
          .body(Map.of("message", "Un département avec ce nom existe déjà"));
    }
  }

  @org.springframework.web.bind.annotation.DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@org.springframework.web.bind.annotation.PathVariable Long id) {
    if (!departments.existsById(id)) {
      return ResponseEntity.notFound().build();
    }

    if (employees.countByDepartmentId(id) > 0) {
      return ResponseEntity.status(409)
          .body(Map.of("message", "Le département contient encore des employés"));
    }

    departments.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
