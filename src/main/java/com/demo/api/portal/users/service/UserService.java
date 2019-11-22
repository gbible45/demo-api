package com.demo.api.portal.users.service;

import com.demo.api.framework.exception.BizException;
import com.demo.api.framework.util.XpertUtil;
import com.demo.api.portal.users.domain.PortalUser;
import com.demo.api.portal.users.dto.LoginRequest;
import com.demo.api.portal.users.dto.UserRequest;
import com.demo.api.portal.users.dto.UserRole;
import com.demo.api.portal.users.repository.UserRepository;
import com.demo.api.portal.users.domain.PortalUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    private static PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${lemon.env.cf.sudouser:}")
    private String sudoUserName;

    @Value("${portal.admin.email:}")
    private String portalAdminEmail;

    @Value("${portal.admin.password:}")
    private String portalAdminPassowrd;

    @Transactional(readOnly = true)
    public PortalUser loginCheck(LoginRequest loginRequest) {
        PortalUser checkPortalUser = getUserByEmail(loginRequest.getEmail());
        if (!passwordEncoder.matches(loginRequest.getPassword(), checkPortalUser.getPassword())) {
            throw new BizException(HttpStatus.UNAUTHORIZED, "portal.login.not_equals_password");
        }
        return checkPortalUser;
    }

	@Transactional(readOnly = true)
    public PortalUser getMe() {
        PortalUser portalUser = XpertUtil.getUser();
        return portalUser;
    }

    @Transactional(readOnly = true)
    public List<PortalUser> listAllUsers()  {
        List<PortalUser> users = userRepository.findAllByDelUserYnOrderByIdDesc("N");
        return users;
    }

	@Transactional(readOnly = true)
    public PortalUser getUserByEmail(String email)  {
        Optional<PortalUser> optionalPortalUser = userRepository.findByEmail(email);
        if (optionalPortalUser.isPresent()) {
            return setPortalUserAuth(optionalPortalUser.get());
        }
        return null;
    }

    @Transactional
    public void deletePortalUser(String email) {
        PortalUser portalUser = getUserByEmail(email);
        deletePortalUser(portalUser);
    }

    @Transactional
    public void deletePortalUser(PortalUser portalUser) {
        portalUser.setDelUserYn("Y");
        userRepository.save(portalUser);
        //userRepository.delete(portalUser);
    }

    @Transactional
    public PortalUser createPortalUser(UserRequest portalUserRequest) {
        PortalUser portalUser = new PortalUser();
        portalUser.setEmail(portalUserRequest.getEmail());
        portalUser.setPassword(passwordEncoder.encode(portalUserRequest.getPassword()));
        portalUser.setName(portalUserRequest.getName());
        portalUser.setChangePasswordYn("N");
        portalUser.setDelUserYn("N");
        if (portalUserRequest.getRoles() != null && portalUserRequest.getRoles().size() > 0) {
            for (UserRole userRole : portalUserRequest.getRoles()) {
                portalUser.getRoles().add(userRole.getValue());
            }
        }
        return createPortalUser(portalUser);
    }

    @Transactional
    public PortalUser createPortalUser(PortalUser portalUser) {
        return userRepository.save(portalUser);
    }

    @Transactional
    public PortalUser changePassword(String oldPassword, String newPassword) {
        PortalUser sessinoPortalUser = XpertUtil.getUser();
        PortalUser portalUser = getUserByEmail(sessinoPortalUser.getEmail());
        if ("Y".equals(portalUser.getChangePasswordYn())) {
            if (!passwordEncoder.matches(oldPassword, portalUser.getPassword())) {
                throw new BizException(HttpStatus.BAD_REQUEST, "portal.user.notEqualsOldPassword");
            }
        }
        portalUser.setPassword(passwordEncoder.encode(newPassword));
        portalUser.setChangePasswordYn("Y");
        portalUser = userRepository.save(portalUser);
        // 섹션에 있는 사용자 정보 변경
        XpertUtil.getUser(portalUser);
        return portalUser;
    }

    @Transactional
    public PortalUser changeUserPassword(String email, String newPassword) {
        PortalUser portalUser = getUserByEmail(email);
        portalUser.setPassword(passwordEncoder.encode(newPassword));
        portalUser.setChangePasswordYn("N");
        return userRepository.save(portalUser);
    }

    @Transactional
    public PortalUser updateUserInfo(String email, UserRequest userRequest) {
        PortalUser portalUser = getUserByEmail(email);
        if (!StringUtils.isEmpty(userRequest.getName())) {
            portalUser.setName(userRequest.getName());
        }
        if (userRequest.getRoles() != null) {
            portalUser.setRoles(new HashSet<>());
            for (UserRole userRole : userRequest.getRoles()) {
                portalUser.getRoles().add(userRole.getValue());
            }
        }
        return userRepository.save(portalUser);
    }

    @Transactional
    public PortalUser resetUserPassword(String email, String newPassword) {
        PortalUser portalUser = getUserByEmail(email);
        //String newPassword = PasswordGenerator.generatePassword(10);
        portalUser.setPassword(passwordEncoder.encode(newPassword));
        portalUser.setChangePasswordYn("N");
        userRepository.save(portalUser);
        return userRepository.save(portalUser);
    }

    @Transactional(readOnly = true)
    public PortalUser setPortalUserAuth(PortalUser portalUser) {
        for (String role : portalUser.getRoles()) {
            if (UserRole.ADMIN.equals(UserRole.from(role))) {
                portalUser.setAdmin(true);
            } else if (portalUser.isDeveloper()) {
                portalUser.setDeveloper(true);
            } else if (portalUser.isAuditor()) {
                portalUser.setAuditor(true);
            } else if (portalUser.isMonitor()) {
                portalUser.setMonitor(true);
            }
        }
        return portalUser;
    }

    @Transactional
    public PortalUser updatePortalUser (PortalUser portalUser) {
        portalUser = userRepository.save(portalUser);
        return portalUser;
    }
}