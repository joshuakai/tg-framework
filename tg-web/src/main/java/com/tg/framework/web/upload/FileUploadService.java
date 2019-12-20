package com.tg.framework.web.upload;

import com.tg.framework.commons.http.RequestDetails;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

  String[] store(MultipartFile[] multipartFiles, RequestDetails requestDetails);

  String store(MultipartFile multipartFile, RequestDetails requestDetails);

}
