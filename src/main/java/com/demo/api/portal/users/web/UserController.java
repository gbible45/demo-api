package com.demo.api.portal.users.web;

import com.demo.api.portal.users.domain.PortalUser;
import com.demo.api.portal.users.dto.ChangePWRequest;
import com.demo.api.portal.users.dto.UserRequest;
import com.demo.api.portal.users.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "${portal.api.prefix:/api/portal}/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    private String email;
    private ChangePWRequest changePWRequest;

    @ApiOperation(value = "사용자 목록", notes = "관련 페이지: 사용자 관리")
    @RequestMapping(method = RequestMethod.GET, value="/")
    @PreAuthorize("hasRole('ROLE_GOOD_ADMIN')")
    public List<PortalUser> listAllUsers() {
        return PortalUser.toDTO(userService.listAllUsers());
    }

    @ApiOperation(value = "사용자 정보 by Email : ROLE_GOOD_USER")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "사용자 Email", required = true, dataType = "string", paramType = "path", defaultValue = ""),
    })
    @RequestMapping(method = RequestMethod.GET, value="/{email}/")
    @PreAuthorize("hasRole('ROLE_GOOD_ADMIN')")
    public PortalUser getUserByEmail(@PathVariable String email) {
        PortalUser portalUser = userService.getUserByEmail(email);
        if (portalUser != null) {
            return portalUser.toDTO();
        }
        return null;
    }

    @ApiOperation(value = "사용자 등록 : ROLE_GOOD_ADMIN")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userRequest", value = "사용자 등록 정보", required = true, dataType = "UserRequest", paramType = "body", defaultValue = "")
    })
    @RequestMapping(method = RequestMethod.POST, value="/")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_GOOD_ADMIN')")
    public PortalUser createPortalUser(@RequestBody UserRequest userRequest) {
        return userService.createPortalUser(userRequest).toDTO();
    }

    @ApiOperation(value = "사용자 삭제 : ROLE_GOOD_ADMIN")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "사용자 Email", required = true, dataType = "string", paramType = "path", defaultValue = ""),
    })
    @RequestMapping(method = RequestMethod.DELETE, value="/{email}/")
    @PreAuthorize("hasRole('ROLE_GOOD_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePortalUser(@PathVariable String email) {
        userService.deletePortalUser(email);
    }

    @ApiOperation(value = "비밀번호 변경 : ROLE_GOOD_USER")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "changePWRequest", value = "비밀번호", required = true, dataType = "ChangePWRequest", paramType = "body", defaultValue = ""),
    })
    @RequestMapping(method = RequestMethod.PUT, value="/change_password/")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_GOOD_USER')")
    public PortalUser changePassword(@RequestBody ChangePWRequest changePWRequest) {
        return userService.changePassword(changePWRequest.getOldPassword(), changePWRequest.getNewPassword()).toDTO();
    }

    @ApiOperation(value = "사용자 비밀번호 변경 : ROLE_GOOD_ADMIN")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "사용자 Email", required = true, dataType = "string", paramType = "path", defaultValue = ""),
            @ApiImplicitParam(name = "changePWRequest", value = "비밀번호", required = true, dataType = "ChangePWRequest", paramType = "body", defaultValue = "")
    })
    @RequestMapping(method = RequestMethod.PUT, value="/{email}/change_password/")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_GOOD_ADMIN')")
    public PortalUser changeUserPassword(@PathVariable String email,
                                    @RequestBody ChangePWRequest changePWRequest) {
        return userService.changeUserPassword(email, changePWRequest.getNewPassword()).toDTO();
    }

    @ApiOperation(value = "사용자 정보 변경 : ROLE_GOOD_ADMIN")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "사용자 Email", required = true, dataType = "string", paramType = "path", defaultValue = ""),
            @ApiImplicitParam(name = "userRequest", value = "사용자 정보", required = true, dataType = "UserRequest", paramType = "body", defaultValue = "")
    })
    @RequestMapping(method = RequestMethod.PUT, value="/{email}/")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_GOOD_ADMIN')")
    public PortalUser updateUserInfo(@PathVariable String email,
                                     @RequestBody UserRequest userRequest) {
        return userService.updateUserInfo(email, userRequest);
    }

    @ApiOperation(value = "사용자 비밀번호 초기화 : ROLE_GOOD_ADMIN")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "사용자 Email", required = true, dataType = "string", paramType = "path", defaultValue = ""),
            @ApiImplicitParam(name = "changePWRequest", value = "비밀번호", required = true, dataType = "ChangePWRequest", paramType = "body", defaultValue = "")
    })
    @RequestMapping(method = RequestMethod.PUT, value="/{email}/reset_password/")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_GOOD_ADMIN')")
    public PortalUser resetUserPassword(@PathVariable String email,
                                     @RequestBody ChangePWRequest changePWRequest) {
        return userService.resetUserPassword(email, changePWRequest.getNewPassword()).toDTO();
    }
}