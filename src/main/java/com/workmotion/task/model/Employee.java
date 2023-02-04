package com.workmotion.task.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String firstName;
	
	@NotNull
	private String lastName;

	private String email;

	@NotNull
	private String phoneNumber;

	private Integer age;
	
	@Min(2000)
	private Double salary;

	@Enumerated(EnumType.STRING)
	private EmployeeState state;

	@Enumerated(EnumType.STRING)
	private EmployeeState securityState;

	@Enumerated(EnumType.STRING)
	private EmployeeState workPermitState;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@PrePersist
	void prePersist() {
		this.createdAt = LocalDateTime.now();
	}

	@PreUpdate
	void postUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}
