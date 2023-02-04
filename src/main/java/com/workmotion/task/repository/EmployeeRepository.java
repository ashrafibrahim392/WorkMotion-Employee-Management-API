package com.workmotion.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workmotion.task.model.Employee;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> , PagingAndSortingRepository<Employee, Long> {

}
