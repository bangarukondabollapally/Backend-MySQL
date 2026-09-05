package com.example.backendmysql.service;

import java.util.List;

import com.example.backendmysql.dto.EmployeeDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backendmysql.entity.Employee;
import com.example.backendmysql.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JWTService JService;

    public List<Employee> getAllEmployees() {
        return repo.findAll();
    }

    public Employee getEmployeeById(Long id){
        return repo.findById(id).orElse(null);
    }

    public Employee createEmployee(@RequestBody @Valid EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setRole(dto.getRole());
        employee.setEmail(dto.getEmail());
        employee.setPassword(encoder.encode(dto.getPassword()));
        return repo.save(employee);
    }

    public Employee updateEmployee(Long id, Employee newemployee) {
        Employee existingEmployee = repo.findById(id).orElse(null);
        if (existingEmployee != null) {
            existingEmployee.setName(newemployee.getName());
            existingEmployee.setRole(newemployee.getRole());
            existingEmployee.setEmail(newemployee.getEmail());
            if (newemployee.getPassword() != null && !newemployee.getPassword().isEmpty()) {
                existingEmployee.setPassword(encoder.encode(newemployee.getPassword()));
            }
            return repo.save(existingEmployee);
        }
        return null;
    }

    public String deleteEmployee(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return "Employee deleted successfully";
        }
        return "employee not found";
    }

    public ResponseEntity<?> login(Employee emp) {
        Employee existingEmp = repo.findByEmail(emp.getEmail());
        if (existingEmp == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Email is wrong");
        }
        if (!encoder.matches(emp.getPassword(), existingEmp.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Password is wrong...");
        }
        String token = JService.generateToken(emp.getEmail());
        return ResponseEntity.ok(token);
    }
}
