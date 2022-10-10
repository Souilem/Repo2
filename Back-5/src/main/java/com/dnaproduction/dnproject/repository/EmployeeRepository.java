package com.dnaproduction.dnproject.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dnaproduction.dnproject.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {


}
