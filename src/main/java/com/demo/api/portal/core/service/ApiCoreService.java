package com.demo.api.portal.core.service;

import com.demo.api.framework.LemonService;
import com.demo.api.framework.domain.AbstractUser;
import com.demo.api.framework.domain.ChangePasswordForm;
import com.demo.api.framework.exception.BizException;
import com.demo.api.framework.validation.Password;
import com.demo.api.portal.core.domain.PortalFile;
import com.demo.api.portal.core.domain.PortalImage;
import com.demo.api.portal.users.domain.PortalUser;
import com.demo.api.portal.users.dto.UserRequest;
import com.demo.api.portal.users.dto.UserRole;
import com.demo.api.portal.users.repository.UserRepository;
import com.demo.api.portal.util.PortalDataInitalizer;
import com.demo.api.portal.util.PortalFileUtil;
import com.demo.api.portal.core.domain.PortalFile;
import com.demo.api.portal.core.domain.PortalImage;
import com.demo.api.portal.util.PortalDataInitalizer;
import com.demo.api.portal.util.PortalFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.util.List;

@Service
@Slf4j
public class ApiCoreService extends LemonService<PortalUser, Long> {

    public static final String ADMIN_NAME = "관리자";

    @Autowired
    UserRepository userRepository;

    @Autowired
    PortalDataInitalizer portalDataInitalizer;

    @Override
    protected PortalUser newUser() {
        return new PortalUser();
    }

    @Override
    protected PortalUser createAdminUser() {
        PortalUser user = super.createAdminUser();
        user.setName(ADMIN_NAME);
        return user;
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=false)
    public PortalUser firstCreateAdminUser(UserRequest userRequest) {
        List<PortalUser> adminUsers = userRepository.findAllByRoles(UserRole.ADMIN.getValue());
        if (adminUsers != null && adminUsers.size() > 0) {
            throw new BizException(HttpStatus.PAYMENT_REQUIRED, "portal.alreadyCreatedAdmin");
        }
        return firstCreateAdminUser(userRequest);
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=false)
    public PortalUser createAdminUser(UserRequest userRequest) {
        PortalUser user = super.createAdminUser(userRequest.getEmail(), userRequest.getPassword());
        user.setName(userRequest.getName());
        return user;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly=false)
    public void onStartup() {
        portalDataInitalizer.init();
    }

    @Override
    @PreAuthorize("hasPermission(#user, 'edit')")
    @Transactional(propagation= Propagation.REQUIRED, readOnly=false)
    public void changePassword(PortalUser user, @Valid ChangePasswordForm changePasswordForm) {
        super.changePassword(user, changePasswordForm);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly=false)
    public PortalUser resetPassword(@Valid @NotBlank String forgotPasswordCode,
                                    @Valid @Password String newPassword) {
        PortalUser user = super.resetPassword(forgotPasswordCode, newPassword);

        if(user.hasRole(AbstractUser.Role.ADMIN)) // ADMIN 권한은 CF 계정을 가지지 않고 sudouser 만을 사용
            return user;

        return user;
    }

    public static ModelAndView downloadFile(long id) {
        PortalFile portalFile = PortalFileUtil.getFileInfo(id);
        File downloadFile = new File(portalFile.getFilePath());
        return new ModelAndView(portalFile.getOriginalFileName(), "downloadFile", downloadFile);
    }

    public static ModelAndView downloadImage(long id) {
        PortalImage portalImage = PortalFileUtil.getImageInfo(id);
        return new ModelAndView(portalImage.getImageName(), "downloadFile", portalImage.getImage());
    }
}
