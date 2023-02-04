package com.workmotion.task.service;

import com.workmotion.task.dto.EmployeeRequestDTO;
import com.workmotion.task.dto.EmployeeResponseDTO;
import com.workmotion.task.exception.FunctionalException;
import com.workmotion.task.model.Employee;
import com.workmotion.task.model.EmployeeStateEvent;

public interface EmployeeService {

	 void changeState (Long employeeId , EmployeeStateEvent event) throws FunctionalException;
	
	 void saveEmployee (EmployeeRequestDTO  employeeRequestDTO );

	EmployeeResponseDTO listEmployeePage (Integer limit , Integer offset );
	 
}
