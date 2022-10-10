package com.dnaproduction.dnproject.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(name="APP_ROLE")
public class AppRole {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String roleName;

}
