package com.demo.api.portal.core.dto;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResult {
    private String fileName;
    private String originalFileName;
    private long fileSize;
    private String contentType;
    private String filePath;
    private String downloadUrl;
}
