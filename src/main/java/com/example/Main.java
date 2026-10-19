package com.example;

import com.example.wegrow.repository.DepartmentRepository;
import com.example.wegrow.repository.EmployeeRepository;
import javax.sql.DataSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Main
 */
public class Main {
  public static void main(String[] args) {
    try (ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml")) {

      DataSource ds = ctx.getBean("dataSource", DataSource.class);
      try (var con = ds.getConnection()) {
        System.out.println("DB connected: " + con.getMetaData().getURL()
            + " as " + con.getMetaData().getUserName());
      }

      JdbcTemplate jdbc = ctx.getBean("jdbcTemplate", JdbcTemplate.class);
      System.out.println("SELECT 1 -> " + jdbc.queryForObject("SELECT 1", Integer.class));
      System.out.println("departments count -> "
          + jdbc.queryForObject("SELECT COUNT(*) FROM departments", Long.class));
      System.out.println("employees count -> "
          + jdbc.queryForObject("SELECT COUNT(*) FROM employees", Long.class));

      EmployeeRepository employees = ctx.getBean(EmployeeRepository.class);
      DepartmentRepository departments = ctx.getBean(DepartmentRepository.class);
      System.out.println("EmployeeRepository -> " + employees.getClass().getName()
          + ", count=" + employees.count());
      System.out.println("DepartmentRepository -> " + departments.getClass().getName()
          + ", count=" + departments.count());

    } catch (Exception e) {
      System.err.println("DB connection FAILED: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }
}
