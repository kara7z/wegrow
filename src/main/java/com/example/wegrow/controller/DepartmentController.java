package com.example.wegrow.controller;

import com.example.wegrow.model.Department;
import com.example.wegrow.repository.DepartmentRepository;
import java.util.Map;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
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

  public DepartmentController(DepartmentRepository departments) {
    this.departments = departments;
  }

  public record DeptRequest(String nom, String description) {
  }

  public record DeptResponse(Long id, String nom, String description) {
    static DeptResponse from(Department d) {
      return new DeptResponse(d.getId(), d.getName(), d.getDescription());
    }
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
}
