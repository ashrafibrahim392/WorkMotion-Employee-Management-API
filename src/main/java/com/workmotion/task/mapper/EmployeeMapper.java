package com.workmotion.task.mapper;

import com.workmotion.task.dto.EmployeeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.workmotion.task.dto.EmployeeRequestDTO;
import com.workmotion.task.model.Employee;
import com.workmotion.task.model.EmployeeState;

import java.util.List;

@Mapper(uses = EmployeeState.class)
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(target = "state", expression = "java(com.workmotion.task.model.EmployeeState.ADDED)")
    Employee toEmployee(EmployeeRequestDTO employeeRequestDTO);

    EmployeeDTO toEmployeeDTo(Employee employee);

}
