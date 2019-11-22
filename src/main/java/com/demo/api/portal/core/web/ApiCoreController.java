package com.demo.api.portal.core.web;

import com.demo.api.framework.LemonController;
import com.demo.api.portal.core.dto.ApiServer;
import com.demo.api.portal.core.service.ApiCoreService;
import com.demo.api.portal.users.domain.PortalUser;
import com.demo.api.portal.users.dto.UserRequest;
import com.demo.api.portal.util.PortalConstant;
import com.demo.api.framework.LemonController;
import com.demo.api.portal.core.service.ApiCoreService;
import com.demo.api.portal.users.domain.PortalUser;
import com.demo.api.portal.users.dto.UserRequest;
import com.demo.api.portal.util.PortalConstant;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
@Slf4j
public class ApiCoreController extends LemonController<PortalUser, Long> {

    @Autowired
    private ApiCoreService apiCoreService;

    @RequestMapping(value="/core/ping/", method= RequestMethod.GET)
    public void ping() {
    }

    @RequestMapping(value="/core/server_host_url/", method= RequestMethod.GET)
    public ApiServer serverHostUrl() {
        ApiServer apiServer = new ApiServer();
        apiServer.setApiServerUrl(PortalConstant.getApiServerUrl());
        return apiServer;
    }

    @ApiOperation(value = "최초 관리자 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "UserRequest", value = "사용자 등록 정보", required = true, dataType = "UserRequest", paramType = "body", defaultValue = "")
    })
    @RequestMapping(method = RequestMethod.POST, value="/first/admin/")
    @ResponseStatus(HttpStatus.CREATED)
    public PortalUser firstCreateAdminUser(@RequestBody UserRequest userRequest) {
        return apiCoreService.firstCreateAdminUser(userRequest);
    }

    @ApiOperation(value = "Download file ALL : ROLE_GOOD_ADMIN")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-HEADER-TOKEN", value = "UAA Token 정보", required = true, dataType = "string", paramType = "header", defaultValue = "ADMIN_ACCESS_TOKEN"),
            @ApiImplicitParam(name = "X-HEADER-REGION", value = "Region 정보", required = true, dataType = "int", paramType = "header", defaultValue = "1"),
            @ApiImplicitParam(name = "id", value = "파일 id", required = true, dataType = "int", paramType = "path", defaultValue = "")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/files/{id}/download/")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView downloadFile(@PathVariable long id) throws Exception {
        return apiCoreService.downloadFile(id);
    }

    @ApiOperation(value = "Download Image : ROLE_GOOD_USER")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-HEADER-TOKEN", value = "UAA Token 정보", required = true, dataType = "string", paramType = "header", defaultValue = "ADMIN_ACCESS_TOKEN"),
            @ApiImplicitParam(name = "X-HEADER-REGION", value = "Region 정보", required = true, dataType = "int", paramType = "header", defaultValue = "1"),
            @ApiImplicitParam(name = "id", value = "파일 id", required = true, dataType = "int", paramType = "path", defaultValue = "")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/images/{id}/download/")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView downloadImage(@PathVariable long id) throws Exception {
        return apiCoreService.downloadImage(id);
    }

}