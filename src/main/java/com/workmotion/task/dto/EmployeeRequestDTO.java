package com.workmotion.task.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.SuperBuilder;


@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDTO {

	@NotNull (message = "First name can't be null")
	private String firstName;
	@NotNull (message = "Last name can't be null")
	private String lastName;
	
	@Email (message = "Please Enter valid Email")
	private String email;
	
	@NotNull (message = "Phone number can't be null")
	private String phoneNumber;

	private Integer age;

	@Min(value=2000 , message = "Min salary value = 2000")
	private Double salary;
}
