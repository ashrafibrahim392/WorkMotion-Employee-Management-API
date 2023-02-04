package com.workmotion.task.controller;


import com.workmotion.task.model.Employee;
import com.workmotion.task.repository.EmployeeRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Health check Controller", description = "Health check for microservice")
public class HealthCheckController {
    private final EmployeeRepository employeeRepository;
    @GetMapping("/ready")
    String isReady (){
        return "Ready";
    }

    @GetMapping("/live")
    String isLive (){
        employeeRepository.findAll();
        return "Live";
    }
}
