package com.demo.api.portal.util;

import com.demo.api.portal.core.domain.PortalFile;
import com.demo.api.portal.core.domain.PortalImage;
import com.demo.api.portal.core.dto.FileUploadResult;
import com.demo.api.portal.core.repository.PortalFileRepository;
import com.demo.api.portal.core.repository.PortalImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Component
@Slf4j
public class PortalFileUtil {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    private static PortalFileRepository portalFileRepository;

    @Autowired
    public void setPortalFileRepository(PortalFileRepository portalFileRepository) {
        PortalFileUtil.portalFileRepository = portalFileRepository;
    }

    private static PortalImageRepository portalImageRepository;

    @Autowired
    public void setPortalImageRepository(PortalImageRepository portalImageRepository) {
        PortalFileUtil.portalImageRepository = portalImageRepository;
    }

    private static String uploadPath;

    @Autowired
    public void setUploadPath(@Value("${portal.file.default.uploadPath:}") String uploadPath) {
        PortalFileUtil.uploadPath = uploadPath;
        CommUtil.checkDirToMakeDirs(uploadPath);
    }

    private static String downloadPath;

    @Autowired
    public void setDownloadPath(@Value("${portal.file.default.downloadPath:}") String downloadPath) {
        PortalFileUtil.downloadPath = downloadPath;
    }

    private static String deployUploadPath;

    @Autowired
    public void setDeployUploadPath(@Value("${portal.file.deploy.uploadPath:}") String deployUploadPath) {
        PortalFileUtil.deployUploadPath = deployUploadPath;
        CommUtil.checkDirToMakeDirs(uploadPath);
    }

    private static String deployDownloadPath;

    @Autowired
    public void setDeployDownloadPath(@Value("${portal.file.deploy.downloadPath:}") String deployDownloadPath) {
        PortalFileUtil.deployDownloadPath = deployDownloadPath;
    }

    public PortalFileUtil() {}

    public static ReadFileBytes readMultipartFileBytes(MultipartFile file) {
        ReadFileBytes readFileBytes = new ReadFileBytes();
        if(file != null) {
            readFileBytes.setFileName(file.getOriginalFilename());
            try{
                readFileBytes.setFileBytes(file.getBytes());
                readFileBytes.setContentType(file.getContentType());
            } catch (Exception e) {
                log.error("e.getMessage(): {}", e.getMessage());
                e.printStackTrace();
            }
        }
        return readFileBytes;
    }

    private static String getAddTimeFileName(String fileName) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	    fileName = fileName.replaceAll(" ", "_");
        int idx = fileName.lastIndexOf(".");
        if (idx < 0) {
            fileName = fileName + "_" + simpleDateFormat.format(timestamp);
        } else {
            fileName = fileName.substring(0, idx) + "_" + simpleDateFormat.format(timestamp) + fileName.substring(idx);
        }
        return fileName;
    }

    private static FileUploadResult targetFileUpload(String target, String subPath, MultipartFile file) throws IOException {
        FileUploadResult result = new FileUploadResult();
        String originalFilename = file.getOriginalFilename();
        String fileName = getAddTimeFileName(originalFilename);
        String contentType = file.getContentType();
        result.setFileName(fileName);
        result.setOriginalFileName(originalFilename);
        result.setFileSize(file.getSize());
        result.setContentType(contentType);
        log.debug("target: {}", target.toUpperCase());
        try {
            String fullFilePath = ((target == "deploy") ? deployUploadPath : uploadPath) + "/" + subPath + "/" + fileName;
            File newFile = new File(fullFilePath);
            file.transferTo(newFile);
            result.setFilePath(newFile.getPath());
            result.setDownloadUrl(((target == "deploy") ? deployDownloadPath : downloadPath) + "/" + subPath + "/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    private static void targetDeleteFile(String target, String subPath, String fileName) {
        String uploadDirPath = ((target == "deploy") ? deployUploadPath : uploadPath) + "/" + subPath;
        File uploadDir = new File(uploadDirPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String fullFilePath = uploadDirPath + "/" + fileName;
        File file = new File(fullFilePath);
        file.delete();
    }

    public static PortalFile updateFile(long id, MultipartFile file) throws IOException {
        PortalFile portalFile = portalFileRepository.findById(id);
        return updateFile(portalFile, file);
    }

    public static PortalFile updateFile(PortalFile portalFile, MultipartFile file) throws IOException {
        if (portalFile == null) return portalFile;
        if (file != null && !file.isEmpty()) {
            targetDeleteFile(portalFile.getTarget(), portalFile.getSubPath(), portalFile.getFileName());
            FileUploadResult upload = targetFileUpload(portalFile.getTarget(), portalFile.getSubPath(), file);
            portalFile.setContentType(upload.getContentType());
            portalFile.setFileName(upload.getFileName());
            portalFile.setOriginalFileName(upload.getOriginalFileName());
            portalFile.setFileSize(file.getSize());
            portalFile.setFilePath(upload.getFilePath());
            portalFile = portalFileRepository.save(portalFile);
        }
        return portalFile;
    }

    public static PortalFile fileUpload(String target, String subPath, MultipartFile file, String tableName, String fieldName) throws IOException {
        return fileUpload(target, subPath, file, tableName, fieldName, null);
    }

    public static PortalFile fileUpload(String target, String subPath, MultipartFile file, String tableName, String fieldName, Long parentId) throws IOException {
        log.debug("target: {}", target.toUpperCase());
        String baseUploadPath = ((target == "deploy") ? deployUploadPath : uploadPath);
        File uploadDir = new File(baseUploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String subUploadPath = baseUploadPath + "/" + subPath;
        File uploadBucketDir = new File(subUploadPath);
        if (!uploadBucketDir.exists()) {
            uploadBucketDir.mkdirs();
        }
        PortalFile portalFile = null;
        if (file != null && !file.isEmpty()) {
            FileUploadResult upload = targetFileUpload(target, subPath, file);
            portalFile = new PortalFile(target, subPath, upload.getContentType(), upload.getFileName(), upload.getOriginalFileName(), upload.getFileSize(), upload.getFilePath(), upload.getDownloadUrl(), tableName, fieldName, parentId);
            portalFile = portalFileRepository.save(portalFile);
        }
        return portalFile;
    }

    public static PortalFile getFileInfo(long id) {
        return portalFileRepository.findById(id);
    }

    public static PortalFile updateFileInfo(PortalFile portalFile) {
        return portalFileRepository.save(portalFile);
    }

    public static void deleteFile(PortalFile portalFile) {
        if (portalFile == null) return;
        targetDeleteFile(portalFile.getTarget(), portalFile.getSubPath(), portalFile.getFileName());
        portalFileRepository.deleteById(portalFile.getId());
    }

    public static void deleteFile(long id) {
        PortalFile portalFile = portalFileRepository.findById(id);
        deleteFile(portalFile);
    }

    public static PortalImage imageUpload(MultipartFile file, String tableName, String fieldName) {
        return imageUpload(file, tableName, fieldName, null);
    }

    public static PortalImage imageUpload(MultipartFile file, String tableName, String fieldName, Long parentId) {
        PortalImage portalImage = null;
        if (file != null && !file.isEmpty()) {
            try {
                portalImage = new PortalImage(file.getContentType(), file.getOriginalFilename(), file.getBytes(), file.getSize(), tableName, fieldName, parentId);
                portalImage = portalImageRepository.save(portalImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return portalImage;
    }

    public static PortalImage updateImage(long id, MultipartFile file) {
        PortalImage portalImage = portalImageRepository.findById(id);
        return updateImage(portalImage, file);
    }

    public static PortalImage updateImage(PortalImage portalImage, MultipartFile file) {
        if (portalImage == null) return portalImage;
        if (file != null && !file.isEmpty()) {
            try {
                portalImage.setContentType(file.getContentType());
                portalImage.setImageName(file.getOriginalFilename());
                portalImage.setImage(file.getBytes());
                portalImage.setFileSize(file.getSize());
                portalImage = portalImageRepository.save(portalImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return portalImage;
    }

    public static PortalImage getImageInfo(long id) {
        return portalImageRepository.findById(id);
    }

    public static PortalImage updateImageInfo(PortalImage portalImage) {
        if (portalImage == null) return portalImage;
        return portalImageRepository.save(portalImage);
    }

    public static void deleteImage(long id) {
        portalImageRepository.deleteById(id);
    }

    public static File createTemporaryUploadFile(MultipartFile file) throws IOException {
        return createTemporaryUploadFile(file.getOriginalFilename(), file.getInputStream());
    }

    public static File createTemporaryUploadFile(String fileName, InputStream inputStream) throws IOException {
        String tmpFileName = getAddTimeFileName(fileName);
        File file = File.createTempFile(tmpFileName, null);
        FileOutputStream outputStream = new FileOutputStream(file);
        FileCopyUtils.copy(inputStream, outputStream);
        outputStream.close();
        return file;
    }

}