package com.demo.api.portal.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRolesRequest implements Serializable {
	@NotNull
	private List<UserRole> roles;
}
