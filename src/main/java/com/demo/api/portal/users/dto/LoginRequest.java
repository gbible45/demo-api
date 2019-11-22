package com.demo.api.portal.users.dto;

import com.demo.api.framework.validation.Password;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest implements Serializable {
	@NotNull
	private String email;

	@NotNull
	@Password
	private String password;

}
