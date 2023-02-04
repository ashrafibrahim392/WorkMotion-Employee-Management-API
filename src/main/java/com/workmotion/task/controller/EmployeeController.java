package com.workmotion.task.controller;

import javax.validation.Valid;

import com.workmotion.task.dto.EmployeeResponseDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.workmotion.task.dto.EmployeeRequestDTO;
import com.workmotion.task.dto.ResponseMessage;
import com.workmotion.task.exception.FunctionalException;
import com.workmotion.task.model.EmployeeStateEvent;
import com.workmotion.task.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/employee")
@Slf4j
@Tag(name = "Employee Controller", description = "Employee controller to add ,edit and list employee")
public class EmployeeController {
	private final EmployeeService employeeService;
	
    @PutMapping(value = "/{id}/state/{event}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "403", description = "Not valid action ")
    })

    public ResponseEntity<ResponseMessage> updateState(@PathVariable Long id , @PathVariable EmployeeStateEvent event) throws FunctionalException {
        employeeService.changeState(id , event);
        return new ResponseEntity<>(new ResponseMessage("EMPLOYEE_STATE_UPDATED_SUCCESSFULLY"), HttpStatus.OK);
    }
    
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added")
    })
    public ResponseEntity<ResponseMessage> save (@Valid @RequestBody EmployeeRequestDTO employeeRequestDTO)
    {
    	employeeService.saveEmployee(employeeRequestDTO);
    	return new ResponseEntity<>(new ResponseMessage("EMPLOYEE_ADDED_SUCCESSFULLY"), HttpStatus.CREATED);
    }

    @GetMapping ("/employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    public ResponseEntity<EmployeeResponseDTO> listEmployeePage (@RequestParam(defaultValue = "10") Integer limit , @RequestParam(defaultValue = "0") Integer offset )
    {
        return new ResponseEntity<>(employeeService.listEmployeePage(limit,offset), HttpStatus.OK);
    }
}
