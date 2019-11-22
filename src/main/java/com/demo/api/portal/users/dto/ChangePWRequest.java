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
public class ChangePWRequest implements Serializable {
	@NotNull
	@Password
	private String oldPassword;

	@NotNull
	@Password
	private String newPassword;
}
