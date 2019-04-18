package com.atguigu.gmall.manage.controller;

import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(description = "fastDfs的文件上传功能")
@RestController
@CrossOrigin
public class FileUploadController {

    @Value("${fileServer.url}")
    private String fileUrl;
    //http://localhost:8082/fileUpload
    @RequestMapping(value = "fileUpload",method = RequestMethod.POST)
    public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException, MyException {
        String congFile = this.getClass().getResource("/tracker.conf").getFile();
        String imgUrl = fileUrl;
        ClientGlobal.init(congFile);
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        //String orginalFilename = "C:\\Users\\25230\\Desktop\\tupian\\c.jpg";
        String originalFilename = file.getOriginalFilename();
        String extName = StringUtils.substringAfter(originalFilename, ".");

        String[] upload_file = storageClient.upload_file(file.getBytes(), extName, null);
        for (int i = 0; i < upload_file.length; i++) {
            String path = upload_file[i];
            imgUrl += "/" + path;
            System.out.println("imgUrl = " + imgUrl);
        }
       return  imgUrl;
    }
}
