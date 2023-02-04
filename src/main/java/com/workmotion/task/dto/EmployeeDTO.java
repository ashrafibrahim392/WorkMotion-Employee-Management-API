package com.workmotion.task.dto;

import com.workmotion.task.model.EmployeeState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO extends EmployeeRequestDTO {

    private Long id;

    private EmployeeState state;

    private EmployeeState securityState;

    private EmployeeState workPermitState;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
