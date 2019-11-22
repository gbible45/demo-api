package com.demo.api.portal.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;

@Component
@Slf4j
public class PortalConstant {

    public static class CheckRunType {
        public static final String RUN = "run";
        public static final String EXECUTE = "execute";
    }

    public static class Prefix {
        public static final String CFGE_EXIST = "exist-";
        public static final String CFGE_ADD = "add-";
    }

    public static class DeployNodeStatusType {
        public static final String EXIST = "exist";
        public static final String ADD = "add";
        public static final String NONE = "none";
        public static final String ALL = "all";
    }

    public static class RotationExecuteType {
        public static final String SERIAL = "serial";
        public static final String PARALLEL = "parallel";
    }

    public static class CmdType {
        public static final String CMD = "cmd";
        public static final String PLAYBOOK = "playbook";
    }

    public static class DeployScale {
        public static final String T1 = "t1";
        public static final String T2 = "t2";
        public static final String T3 = "t3";
    }

    public static class EnvVarType {
        public static final String ENTER = "enter";
        public static final String VAR = "var";
        public static final String VAR_STR = "var_str";
        public static final String EXPORT = "export";
        public static final String EXPORT_STR = "export_str";
        public static final String CUSTOM = "custom";
    }

    public static class NodePowerType {
        public static final String IPMI = "ipmi";
        public static final String VIRSH = "virsh";
    }

    public static String[] DeployNodeTypeOrder = new String[]{"controller", "network", "message", "compute", "storage"};

    public static class DeployNodeType {
        public static final String CONTROLLER = "controller";
        public static final String NETWORK = "network";
        public static final String MESSAGE = "message";
        public static final String COMPUTE = "compute";
        public static final String STORAGE = "storage";
        public static final String ALL = "all";
        public static final String NONE  = "none";
        public static final String STATIC  = "static";
    }

    public static class DeployInterfaceType {
        public static final String MGMT = "mgmt";
        public static final String API = "api";
        public static final String API_BR = "api";
        public static final String API_NOIP = "api_noip";
        public static final String CEPH_CLUSTER = "ceph_cluster";
        public static final String CEPH_SVR = "ceph_svr";
        public static final String EXTERNAL = "external";
        public static final String EXTERNAL_NOIP = "external_noip";
    }

    public static class DeployEnvFabrics {
        public static final String MGMT = "mgmt";
        public static final String API = "api";
        public static final String CEPH_CLUSTER = "ceph-cluster";
        public static final String CEPH_SVR = "ceph-svr";
    }

    public static class CfgType {
        public static final String IP = "ip";
        public static final String IP_CHILD = "ip_child";
    }

    public static class DeployType {
        public static final String FIRST = "first";
        public static final String APPEND = "append";
    }

    public static class RunMode {
        public static final String STEP = "step";
        public static final String AUTO = "auto";
    }

    public static class ConfigStatus {
        public static final String NONE = "none";
        public static final String ING = "ing";
        public static final String DEPLOYED = "deployed";
    }

    public static class DeployNodeStatus {
        public static final String NONE = "none";
        public static final String ING = "ing";
        public static final String DEPLOYED = "deployed";
    }

    public static class DeployStatus {
        public static final String NONE = "none";
        public static final String ING = "ing";
        public static final String STOPPING = "stopping";
        public static final String STOP = "stop";
        public static final String ERROR = "error";
        public static final String DEPLOYED = "deployed";
    }

    public static final String DEPLOY_SERVICE_TYPE_NONE = "none";

    public static class RunStatus {
        public static final String NONE = "none";
        public static final String RUN = "run";
        public static final String END = "end";
    }

    public static class ResultStatus {
        public static final String NONE = "none";
        public static final String ERROR = "error";
        public static final String SUCCESS = "success";
        public static final String CANCEL = "cancel";
    }


    public static class SUBNET_MODE {
        public static final String STATIC = "STATIC";
        public static final String LINK_UP = "LINK_UP";
    }

    private static String ansibleBaseOriginPath;

    @Autowired
    public void setAnsibleBaseOriginPath(@Value("${portal.ansibleBaseOriginPath:}") String ansibleBaseOriginPath) {
        PortalConstant.ansibleBaseOriginPath = ansibleBaseOriginPath;
    }

    public static String getAnsibleBaseOriginPath() {
        return PortalConstant.ansibleBaseOriginPath;
    }

    private static String ansibleBasePath;

    @Autowired
    public void setAnsibleBasePath(@Value("${portal.ansibleBasePath:}") String ansibleBasePath) {
        PortalConstant.ansibleBasePath = ansibleBasePath;
    }

    public static String getAnsibleBasePath() {
        return PortalConstant.ansibleBasePath;
    }

    private static String apiServerHost;

    @Autowired
    public void setApiServerHost(@Value("${portal.apiServerHost:}") String apiServerHost) {
        PortalConstant.apiServerHost = apiServerHost;
    }

    public static String getApiServerHost() {
        return PortalConstant.apiServerHost;
    }

    private static String apiServerPort;

    @Autowired
    public void setApiServerPort(@Value("${server.port:8091}") String apiServerPort) {
        PortalConstant.apiServerPort = apiServerPort;
    }

    public static String getApiServerPort() {
        return PortalConstant.apiServerPort;
    }


    private static String maasControllerIp;

    @Autowired
    public void setMaasControllerIp(@Value("${maas.api.maasControllerIp:}") String maasControllerIp) {
        PortalConstant.maasControllerIp = maasControllerIp;
    }

    public static String getMaasControllerIp() {
        return PortalConstant.maasControllerIp;
    }

    private static String rotationExecuteType;

    @Autowired
    public void setRotationExecuteType(@Value("${maas.api.rotationExecuteType:parallel}") String rotationExecuteType) {
        PortalConstant.rotationExecuteType = rotationExecuteType;
    }

    public static String getRotationExecuteType() {
        return PortalConstant.rotationExecuteType;
    }

    private static Long rotationStepMicroTime;

    @Autowired
    public void setRotationStepMicroTime(@Value("${maas.api.rotationStepMicroTime:1000}") Long rotationStepMicroTime) {
        PortalConstant.rotationStepMicroTime = rotationStepMicroTime;
    }

    public static Long getRotationStepMicroTime() {
        return PortalConstant.rotationStepMicroTime;
    }

    public static String getApiServerUrl() {
        if (StringUtils.isEmpty(PortalConstant.apiServerHost)) {
            return PortalConstant.apiServerPort;
        } else {
            if (PortalConstant.apiServerPort.equals("80")) {
                return "http://" + PortalConstant.apiServerHost;
            } else {
                return "http://" + PortalConstant.apiServerHost + ":" + PortalConstant.apiServerPort;
            }
        }
    }

    private static String ansibleCopyShOriginFile;

    @Autowired
    public void setAnsibleCopyShOriginFile(@Value("${ansible.ansibleCopyShOriginFile:}") String ansibleCopyShOriginFile) {
        PortalConstant.ansibleCopyShOriginFile = ansibleCopyShOriginFile;
    }

    public static String getAnsibleCopyShOriginFile() {
        return PortalConstant.ansibleCopyShOriginFile;
    }

    private static String ansibleCopyShFile;

    @Autowired
    public void setAnsibleCopyShFile(@Value("${ansible.ansibleCopyShFile:}") String ansibleCopyShFile) {
        PortalConstant.ansibleCopyShFile = ansibleCopyShFile;
    }

    public static String getAnsibleCopyShFile() {
        return PortalConstant.ansibleCopyShFile;
    }

    private static String deleteKonwnHostShFile;

    @Autowired
    public void setDeleteKonwnHostShFile(@Value("${ansible.deleteKonwnHostShFile:}") String deleteKonwnHostShFile) {
        PortalConstant.deleteKonwnHostShFile = deleteKonwnHostShFile;
    }

    public static String getDeleteKonwnHostShFile() {
        return PortalConstant.deleteKonwnHostShFile;
    }

    private static String[] ansiblePlaybookSh;

    @Autowired
    public void setAnsiblePlaybookSh(@Value("${ansible.playbookSh:}") String[] ansiblePlaybookSh) {
        PortalConstant.ansiblePlaybookSh = ansiblePlaybookSh;
    }

    public static String[] getAnsiblePlaybookSh() {
        return PortalConstant.ansiblePlaybookSh;
    }

    private static String[] nodeDelPartitionSh;

    @Autowired
    public void setNodeDelPartitionSh(@Value("${ansible.nodeDelPartitionSh:}") String[] nodeDelPartitionSh) {
        PortalConstant.nodeDelPartitionSh = nodeDelPartitionSh;
    }

    public static String[] getNodeDelPartitionSh() {
        return PortalConstant.nodeDelPartitionSh;
    }

    private static String shEnterChar;

    @Autowired
    public void setShEnterChar(@Value("${ansible.shEnterChar:\n}") String shEnterChar) {
        PortalConstant.shEnterChar = shEnterChar;
    }

    public static String getShEnterChar() {
        return PortalConstant.shEnterChar;
    }

    private static String executorLogPath;

    @Autowired
    public void setExecutorLogPath(@Value("${ansible.executorLogPath:}") String executorLogPath) {
        PortalConstant.executorLogPath = executorLogPath;
        if (!StringUtils.isEmpty(PortalConstant.executorLogPath)) {
            File logDir = new File(PortalConstant.executorLogPath);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
        }
    }

    public static String getExecutorLogPath() {
        return PortalConstant.executorLogPath;
    }

    private static String makeConfigDirPath;

    @Autowired
    public void setMakeConfigDirPath(@Value("${ansible.makeConfigDirPath:}") String makeConfigDirPath) {
        PortalConstant.makeConfigDirPath = makeConfigDirPath;
        if (!StringUtils.isEmpty(PortalConstant.makeConfigDirPath)) {
            File logDir = new File(PortalConstant.makeConfigDirPath);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
        }
    }

    public static String getMakeConfigDirPath() {
        return PortalConstant.makeConfigDirPath;
    }

    private static String templateConfigDirPath;

    @Autowired
    public void setTemplateConfigDirPath(@Value("${ansible.templateConfigDirPath:}") String templateConfigDirPath) {
        PortalConstant.templateConfigDirPath = templateConfigDirPath;
    }

    private static String ansibleMakeConfigHome;

    @Autowired
    public void setAnsibleMakeConfigHome(@Value("${ansible.var.apbConfigHome:}") String ansibleMakeConfigHome) {
        PortalConstant.ansibleMakeConfigHome = ansibleMakeConfigHome;
    }

    public static String getAnsibleMakeConfigHome() {
        return PortalConstant.ansibleMakeConfigHome;
    }


    public static String getTemplateConfigDirPath() {
        return PortalConstant.templateConfigDirPath;
    }

    private static String windowsShellCopyShFile;

    @Autowired
    public void setWindowsShellCopyShFile(@Value("${ansible.windows.shellCopyShFile:}") String windowsShellCopyShFile) {
        PortalConstant.windowsShellCopyShFile = windowsShellCopyShFile;
    }

    public static String getWindowsShellCopyShFile() {
        return PortalConstant.windowsShellCopyShFile;
    }

    private static String windowsConfigCopyShFile;

    @Autowired
    public void setWindowsConfigCopyShFile(@Value("${ansible.windows.configCopyShFile:}") String windowsConfigCopyShFile) {
        PortalConstant.windowsConfigCopyShFile = windowsConfigCopyShFile;
    }

    public static String getWindowsConfigCopyShFile() {
        return PortalConstant.windowsConfigCopyShFile;
    }

    private static String windowsAnsibleBasePath;

    @Autowired
    public void setWindowsAnsibleBasePath(@Value("${ansible.windows.ansibleBasePath:}") String windowsAnsibleBasePath) {
        PortalConstant.windowsAnsibleBasePath = windowsAnsibleBasePath;
    }

    public static String getWindowsAnsibleBasePath() {
        return PortalConstant.windowsAnsibleBasePath;
    }

    private static String windowsMakeConfigDirPath;

    @Autowired
    public void setWindowsMakeConfigDirPath(@Value("${ansible.windows.makeConfigDirPath:}") String windowsMakeConfigDirPath) {
        PortalConstant.windowsMakeConfigDirPath = windowsMakeConfigDirPath;
    }

    public static String getWindowsMakeConfigDirPath() {
        return PortalConstant.windowsMakeConfigDirPath;
    }

    public static String getMakeDeployPlaybookCfgFile(Long deployId) {
        return PortalConstant.getMakeConfigDirPath() + "/inventory_" + deployId.toString() + ".cfg";
    }

    public static String getMakeDeployPlaybookCfgOriginFile(Long deployId) {
        return PortalConstant.getMakeConfigDirPath() + "/inventory_" + deployId.toString() + "_origin.cfg";
    }

    public static String getMakeDeployPlaybookEnvFile(Long deployId) {
        return PortalConstant.getMakeConfigDirPath() + "/playbook_"  + deployId.toString() + ".env";
    }

    public static String getMakeDeployBmEnvFile(Long deployId) {
        return PortalConstant.getMakeConfigDirPath() + "/bm_" + deployId.toString() + ".env";
    }

    public static String getMakeDeployOpenstackEnvFile(Long deployId) {
        return PortalConstant.getMakeConfigDirPath() + "/openstack_" + deployId.toString() + ".env";
    }

    public static String getMakeDeployPlaybookVariableFile(Long deployId) {
        return PortalConstant.getMakeConfigDirPath() + "/variable_" + deployId.toString() + ".yml";
    }

    public static String getMakeDeployPlaybookVariableOriginFile(Long deployId) {
        return PortalConstant.getMakeConfigDirPath() + "/variable_" + deployId.toString() + "_origin.yml";
    }

    public static String getDeployPlaybookVariableOriginFile(Long deployId) {
        return PortalConstant.getMakeConfigDirPath() + "/variable_" + deployId.toString() + "_origin.yml";
    }

    public static String getDeployPlaybookVariableTemplateFile(String scale) {
        return PortalConstant.getTemplateConfigDirPath() + "/variable_" + scale + "_template.yml";
    }
}
