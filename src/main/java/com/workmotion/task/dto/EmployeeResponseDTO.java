package com.workmotion.task.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class EmployeeResponseDTO {

    private Long totalCount ;
    private List<EmployeeDTO> employees;
}
