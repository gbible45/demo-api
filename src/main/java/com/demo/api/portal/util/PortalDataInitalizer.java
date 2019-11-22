package com.demo.api.portal.util;

import com.demo.api.framework.domain.AbstractUser;
import com.demo.api.framework.util.XpertUtil;
import com.demo.api.portal.core.repository.PortalFileRepository;
import com.demo.api.portal.users.domain.PortalUser;
import com.demo.api.portal.users.dto.UserRequest;
import com.demo.api.portal.users.dto.UserRole;
import com.demo.api.portal.users.repository.UserRepository;
import com.demo.api.portal.users.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class PortalDataInitalizer {

    private static UserRepository portalUserRepository;

    @Autowired
    public void setPortalUserRepository(UserRepository portalUserRepository) {
        PortalDataInitalizer.portalUserRepository = portalUserRepository;
    }

    private static PortalFileRepository portalFileRepository;

    @Autowired
    public void setPortalFileRepository(PortalFileRepository portalFileRepository) {
        PortalDataInitalizer.portalFileRepository = portalFileRepository;
    }

    private static boolean portalInit;

    @Autowired
    public void setPortalInit(@Value("${portal.init:false}") boolean portalInit) {
        PortalDataInitalizer.portalInit = portalInit;
    }

    private static String portalAdminEmail;

    @Autowired
    public void setPortalAdminId(@Value("${portal.admin.email:}") String portalAdminEmail) {
        PortalDataInitalizer.portalAdminEmail = portalAdminEmail;
    }

    private static String portalAdminName;

    @Autowired
    public void setPortalAdminName(@Value("${portal.admin.name:관리자}") String portalAdminName) {
        PortalDataInitalizer.portalAdminName = portalAdminName;
    }

    private static String portalAdminPassword;

    @Autowired
    public void setPortalAdminPassword(@Value("${portal.admin.password:}") String portalAdminPassword) {
        PortalDataInitalizer.portalAdminPassword = portalAdminPassword;
    }

    private static DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        PortalDataInitalizer.dataSource = dataSource;
    }

    private static PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder)    {
        PortalDataInitalizer.passwordEncoder = passwordEncoder;
    }

    private static PortalUser adminUser;
    private static PortalUser adminUserEncryped;

    private static SslCertificateTruster sslCertificateTruster = SslCertificateTruster.instance;

    public PortalDataInitalizer() {
    }

    public static final PortalUser getAdminUser() {
        return adminUser;
    }

    public static PortalUser getAdminUserEncryped() {
        return adminUserEncryped;
    }

    public static void setAdminUserEncryped(PortalUser adminUserEncryped) {
        PortalDataInitalizer.adminUserEncryped = adminUserEncryped;
    }

    public static void setAdminUser(PortalUser adminUser) {
        PortalDataInitalizer.adminUser = adminUser;
    }

    public static void init() {
        log.debug("initializing database...");

        adminUser = new PortalUser(PortalDataInitalizer.portalAdminEmail, portalAdminPassword, portalAdminName, AbstractUser.Role.ADMIN);
        adminUserEncryped = getEncryptedUser(PortalDataInitalizer.portalAdminEmail);
        if (adminUserEncryped == null) {
            adminUserEncryped = saveEncrypedUser(adminUser);
        }
        adminUser.setIdForClient(adminUserEncryped.getId());

    }

    // repository.save(entity) 명령 이후 entity 변경 시에도 entity 값이 반영됨
    // raw password 저장을 위한 entity와 encryped password 저장을 위한 entity를 별도로 하고 encryped entity만을 persist 용으로 사용
    public static PortalUser getEncrypedPortalUser(PortalUser user) {
        String portalPassword = passwordEncoder.encode(user.getPassword());
        PortalUser result = new PortalUser(user.getEmail(), portalPassword,
                user.getName(), user.getRoles());
        return result;
    }

    public static PortalUser getEncryptedUser(String email) {
        Optional<PortalUser> result = portalUserRepository.findByEmail(email);
        PortalUser user = result.isPresent() ? result.get() : null;
        return user;
    }

    public static PortalUser saveEncrypedUser(PortalUser user) {
        return portalUserRepository.save(getEncrypedPortalUser(user));
    }

    // 인증서 생성
    public static void trustCertificateInternal(String cfTarget) {
        if (cfTarget != null && !"".equals(cfTarget)) {
            try {
                URL cfTargetUrl = new URL(cfTarget);
                String host = cfTargetUrl.getHost();
                if ("https".equals(cfTargetUrl.getProtocol()) && host != null) {
                    int httpsPort = cfTargetUrl.getPort() > 0 ? cfTargetUrl.getPort() : 443;
                    try {
                        sslCertificateTruster.trustCertificateInternal(host, httpsPort, 5000);
                    } catch (Exception e) {
                        log.error("trusting certificate at " + host + ":" + httpsPort + " failed due to " + e);
                        log.error("e.getMessage() : " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } catch (MalformedURLException e1) {
                log.error("Cannot parse CF_TARGET '"+cfTarget+"' as a URL");
                log.error("e.getMessage() : " + e1.getMessage());
                e1.printStackTrace();
            }
        }
    }

    // test를 위한 영역
    private static String testUserName;

    @Autowired
    private void setTestUserName(@Value("${portal.test.user_name:}") String testUserName) {
        PortalDataInitalizer.testUserName = testUserName;
    }

    private static String testManagerName;

    @Autowired
    private void setTestMgUserName(@Value("${portal.test.manager_name:}") String testManagerName) {
        PortalDataInitalizer.testManagerName = testManagerName;
    }

    private static String testQuotaName;

    @Autowired
    private void setTestQuotaName(@Value("${portal.test.quota_name:}") String testQuotaName) {
        PortalDataInitalizer.testQuotaName = testQuotaName;
    }

    private static String testOrganizationName;
    private static String testOrganizationKor;
    private static String testCompanyId;

    @Autowired
    private void setTestOrganizationName(@Value("${portal.test.organization_name:}") String testOrganizationName) {
        PortalDataInitalizer.testOrganizationName = testOrganizationName;
    }

    @Autowired
    private void setTestOrganizationKor(@Value("${portal.test.organization_kor:}") String testOrganizationKor) {
        PortalDataInitalizer.testOrganizationKor = testOrganizationKor;
    }

    @Autowired
    private void setTestCompanyId(@Value("${portal.test.company_id:}") String testCompanyId) {
        PortalDataInitalizer.testCompanyId = testCompanyId;
    }

    private static PortalUser testUser;
    private static PortalUser testManager;

    private static boolean testCreateData = false;

    private static UserService portalUserService;

    @Autowired
    public void setPortalUserService(UserService portalUserService)    {
        PortalDataInitalizer.portalUserService = portalUserService;
    }

    public static PortalUser getTestUser() {
        return testUser;
    }

    public static void setTestUser(PortalUser testUser) {
        PortalDataInitalizer.testUser = testUser;
    }

    public static PortalUser getTestManager() {
        return testManager;
    }

    public static void setTestMgUser(PortalUser testManager) {
        PortalDataInitalizer.testManager = testManager;
    }

    public static boolean isTestCreateData() {
        return testCreateData;
    }

    public static void setTestCreateData(boolean testCreateData) {
        PortalDataInitalizer.testCreateData = testCreateData;
    }

    public static void createTestData() {
        XpertUtil.logIn(getAdminUserEncryped());
        testCreateData = true;
        log.debug("init testManager= " + testUser);
        String testUserPassword = testUserName.split("@")[0];
        testUser = createTestPortalUser(testUserName, testUserPassword, UserRole.DEVELOPER);
        testUser.setPassword(testUserPassword);
        log.debug("init testUser= " + testUser);
    }

    public static PortalUser createTestPortalUser(String email, String userPassword, UserRole userRole) {
        PortalUser portalUser = portalUserService.getUserByEmail(email);
        if (portalUser == null) {
            UserRequest portalUserRequest = new UserRequest();
            portalUserRequest.setEmail(email);
            portalUserRequest.setName("test " + userRole.getValue());
            List<UserRole> userRoles = new ArrayList<>();
            userRoles.add(userRole);
            portalUserRequest.setRoles(userRoles);
            portalUserRequest.setPassword(userPassword);
            portalUser = portalUserService.createPortalUser(portalUserRequest);
        }
        return portalUser;
    }

    public static void destroyTestData() throws Exception {
        XpertUtil.logIn(getAdminUserEncryped());
        destroyTestUsersData();
        if (testManager != null) {
            try {
                portalUserService.deletePortalUser(testManager.getEmail());
            } catch (Exception e) {
                log.error("e.getMessage() : " + e.getMessage());
                e.printStackTrace();
                destroyTestUserData(testManager);
            }
            testManager = null;
        }
        if (testUser != null) {
            try {
                portalUserService.deletePortalUser(testUser.getEmail());
            } catch (Exception e) {
                log.error("e.getMessage() : " + e.getMessage());
                e.printStackTrace();
                destroyTestUserData(testUser);
            }
            testUser = null;
        }

        testCreateData = false;
    }

    public static void destroyTestUsersData() throws Exception {
        List<PortalUser> portalUsers = PortalUtil.selectUserEmailsByLikeEmail("test-user-");
        if (portalUsers != null && portalUsers.size() > 0) {
            for (PortalUser portalUser : portalUsers) {
                try {
                    portalUserService.deletePortalUser(portalUser.getName());
                } catch (Exception e) {
                    log.error("e.getMessage() : " + e.getMessage());
                    e.printStackTrace();
                    destroyTestUserData(portalUser);
                }
            }
        }
    }

    public static void destroyTestUserData(PortalUser portalUser) throws Exception {
        PortalUtil.destroyUserAllData(portalUser);
    }
}
