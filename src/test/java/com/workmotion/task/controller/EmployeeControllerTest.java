package com.workmotion.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workmotion.task.dto.EmployeeDTO;
import com.workmotion.task.dto.EmployeeRequestDTO;
import com.workmotion.task.dto.EmployeeResponseDTO;
import com.workmotion.task.exception.FunctionalException;
import com.workmotion.task.model.Employee;
import com.workmotion.task.model.EmployeeState;
import com.workmotion.task.model.EmployeeStateEvent;
import com.workmotion.task.repository.EmployeeRepository;
import com.workmotion.task.service.EmployeeServiceImp;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private EmployeeServiceImp employeeService;


    @Test
    public void saveEmployee() throws Exception {
        EmployeeRequestDTO e1 = EmployeeRequestDTO.builder()
                .firstName("firstname")
                .lastName("lastname")
                .salary(2001.5)
                .age(20)
                .phoneNumber("01008888882")
                .email("test@test.com")
                .build();
        Employee e = Employee.builder()
                .firstName("firstname")
                .lastName("lastname")
                .salary(2001.5)
                .age(20)
                .phoneNumber("01008888882")
                .email("test@test.com")
                .state(EmployeeState.ADDED)
                .build();
        Mockito.doNothing().when(employeeService).saveEmployee(e1);
        mvc.perform(MockMvcRequestBuilders
                        .post("/v1/employee")
                        .content(new ObjectMapper().writeValueAsString(e1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }


    @Test
    public void whenSaveEmployeeWithOutFirstName() throws Exception {
        EmployeeRequestDTO e1 = EmployeeRequestDTO.builder()

                .lastName("lastname")
                .salary(2001.5)
                .age(20)
                .phoneNumber("01008888882")
                .email("test@test.com")
                .build();
        Employee e = Employee.builder()
                .firstName("firstname")
                .lastName("lastname")
                .salary(2001.5)
                .age(20)
                .phoneNumber("01008888882")
                .email("test@test.com")
                .state(EmployeeState.ADDED)
                .build();
        mvc.perform(MockMvcRequestBuilders
                        .post("/v1/employee")
                        .content(new ObjectMapper().writeValueAsString(e1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void whenSaveEmployeeWithWrongSalary() throws Exception {
        EmployeeRequestDTO e1 = EmployeeRequestDTO.builder()
                .lastName("lastname")
                .salary(200.5)
                .age(20)
                .phoneNumber("01008888882")
                .email("test@test.com")
                .build();
        Employee e = Employee.builder()
                .firstName("firstname")
                .lastName("lastname")
                .salary(2001.5)
                .age(20)
                .phoneNumber("01008888882")
                .email("test@test.com")
                .state(EmployeeState.ADDED)
                .build();
        mvc.perform(MockMvcRequestBuilders
                        .post("/v1/employee")
                        .content(new ObjectMapper().writeValueAsString(e1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenChangeState() throws Exception {
        Long id = 1l;
        Mockito.doNothing().when(employeeService).changeState(Mockito.any(), Mockito.any());
        mvc.perform(MockMvcRequestBuilders.put("/v1/employee/"+id+"/state/"+EmployeeStateEvent.BEGIN_CHECK.name()))
                .andExpect(status().isOk());
    }

    @Test
    void whenChangeStateFailed() throws Exception {
        Long id = 1l;
        Mockito.doThrow(new FunctionalException(HttpStatus.FORBIDDEN, "Transaction not accepted")).when(employeeService).changeState(Mockito.any(), Mockito.any());
        mvc.perform(MockMvcRequestBuilders.put("/v1/employee/"+id+"/state/"+EmployeeStateEvent.BEGIN_CHECK.name()))
                .andExpect(status().isForbidden());
    }


    @Test
    void whenListEmployeePage() throws Exception {
        EmployeeDTO e1 = EmployeeDTO.builder()
                .firstName("firstname")
                .lastName("lastname")
                .salary(2001.5)
                .age(20)
                .phoneNumber("01008888882")
                .email("test@test.com")
                .state(EmployeeState.ADDED)
                .build();
        EmployeeDTO e2 = EmployeeDTO.builder()
                .firstName("firstname")
                .lastName("lastname")
                .salary(2001.5)
                .age(20)
                .phoneNumber("01008888882")
                .email("test@test.com")
                .state(EmployeeState.ADDED)
                .build();

        Mockito.when(employeeService.listEmployeePage(10,0)).thenReturn(EmployeeResponseDTO.builder().totalCount(2l).employees(Arrays.asList(e1,e2)).build());
          mvc.perform(MockMvcRequestBuilders.get("/v1/employee/employees?limit=10&offset=0"))
                  .andDo(MockMvcResultHandlers.print())
                  .andExpect(status().isOk())
                .andExpect(jsonPath("$.employees",hasSize(2)));
    }


}
