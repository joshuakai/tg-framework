package com.tg.framework.web.upload;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

  String[] store(MultipartFile[] multipartFiles, String requestIp);

  String store(MultipartFile multipartFile, String requestIp);

}
