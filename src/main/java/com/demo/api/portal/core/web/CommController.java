package com.demo.api.portal.core.web;

import com.demo.api.framework.util.XpertUtil;
import com.demo.api.portal.core.dto.AuthCheck;
import com.demo.api.portal.core.service.CommService;
import com.demo.api.portal.users.domain.PortalUser;
import com.demo.api.portal.users.dto.LoginRequest;
import com.demo.api.portal.users.service.UserService;
import com.demo.api.framework.util.XpertUtil;
import com.demo.api.portal.core.dto.AuthCheck;
import com.demo.api.portal.core.service.CommService;
import com.demo.api.portal.users.domain.PortalUser;
import com.demo.api.portal.users.dto.LoginRequest;
import com.demo.api.portal.users.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/comm")
@Slf4j
public class CommController {
    private static final Log log = LogFactory.getLog(CommController.class);

    @Autowired
    private CommService commService;

	@Autowired
	private UserService userService;

	@ApiOperation(value = "로그인")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "loginRequest", value = "사용자 등록 정보", required = true, dataType = "LoginRequest", paramType = "body", defaultValue = "")
	})
	@RequestMapping(method = RequestMethod.POST, value="/login/")
	@ResponseStatus(HttpStatus.CREATED)
	public PortalUser login(@RequestBody LoginRequest loginRequest) {
        PortalUser portalUser = userService.loginCheck(loginRequest);
		if (portalUser != null && !StringUtils.isEmpty(portalUser.getEmail()) && !"Y".equals(portalUser.getDelUserYn())) {
			XpertUtil.logIn(portalUser);
			return portalUser.toDTO();
		} else {
			return null;
		}
	}

	@ApiOperation(value = "접속 사용자 정보")
	@RequestMapping(method = RequestMethod.GET, value="/auth_info/")
	public PortalUser getAuthInfo() {
		PortalUser portalUser = userService.getMe();
		if (portalUser != null && !StringUtils.isEmpty(portalUser.getEmail())) {
			return portalUser.toDTO();
		} else {
			return null;
		}
	}

	@ApiOperation(value = "사용자 정보 체크")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "email", value = "사용자 Email", required = true, dataType = "string", paramType = "query", defaultValue = "jasypt_local_pass"),
			@ApiImplicitParam(name = "auth", value = "권한 코드", required = true, dataType = "string", paramType = "query", defaultValue = "")
	})
	@RequestMapping(method = RequestMethod.GET, value="/auth_check/")
	public AuthCheck getAuthCheck(@RequestParam(value="email", required=false) String email) {
		PortalUser portalUser = userService.getMe();
		if (portalUser != null && portalUser.getEmail().equals(email)) {
			AuthCheck authCheck = new AuthCheck();
			authCheck.setEmail(email);
			authCheck.setRoles(portalUser.getRoles());
			return authCheck;
		} else {
			return null;
		}
	}

	@ApiOperation(value = "로그아웃")
	@RequestMapping(method = RequestMethod.DELETE, value="/logout/")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void logout() {
		XpertUtil.logOut();
	}

	@ApiOperation(value = "Encrypt")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "encKey", value = "encKey", required = true, dataType = "string", paramType = "query", defaultValue = "jasypt_local_pass"),
			@ApiImplicitParam(name = "encValue", value = "encValue", required = true, dataType = "string", paramType = "query", defaultValue = "")
	})
	@RequestMapping(method = RequestMethod.GET, value="/encrypt/")
	@ResponseStatus(HttpStatus.OK)
	public String getEncrypt(@RequestParam(value="encKey", required=false) String encKey,
	                           @RequestParam(value="encValue", required=false) String encValue) {
		return  commService.getEncrypt(encKey, encValue);
	}

	@ApiOperation(value = "Decrypt")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "encKey", value = "encKey", required = true, dataType = "string", paramType = "query", defaultValue = "jasypt_local_pass"),
			@ApiImplicitParam(name = "encValue", value = "encValue", required = true, dataType = "string", paramType = "query", defaultValue = "")
	})
	@RequestMapping(method = RequestMethod.GET, value="/decrypt/")
	@ResponseStatus(HttpStatus.OK)
	public String getDecrypt(@RequestParam(value="encKey", required=false) String encKey,
	                         @RequestParam(value="encValue", required=false) String encValue) {
		return  commService.getDecrypt(encKey, encValue);
	}

}