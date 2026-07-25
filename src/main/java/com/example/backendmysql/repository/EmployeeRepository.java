package com.example.backendmysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backendmysql.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}