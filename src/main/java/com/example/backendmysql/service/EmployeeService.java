package com.example.backendmysql.service;

import java.util.List;

import com.example.backendmysql.dto.EmployeeDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backendmysql.entity.Employee;
import com.example.backendmysql.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository repo;

    public List<Employee> getAllEmployees() {
        return repo.findAll();
    }

    public Employee createEmployee(@RequestBody @Valid EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setRole(dto.getRole());
        employee.setEmail(dto.getEmail());
        employee.setPassword(dto.getPassword());
        return repo.save(employee);
    }

    public Employee updateEmployee(Long id, Employee newemployee) {
        Employee existingEmployee = repo.findById(id).orElse(null);
        if (existingEmployee != null) {
            existingEmployee.setName(newemployee.getName());
            existingEmployee.setRole(newemployee.getRole());
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
}
