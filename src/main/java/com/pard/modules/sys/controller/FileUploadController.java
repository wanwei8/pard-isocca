package com.pard.modules.sys.controller;

import com.google.common.collect.Lists;
import com.pard.common.controller.GenericController;
import com.pard.common.message.ResponseMessage;
import com.pard.common.utils.DateTimeUtils;
import com.pard.modules.sys.entity.User;
import com.pard.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

/**
 * Created by wawe on 17/6/10.
 */
@Controller
public class FileUploadController extends GenericController {
    private final ResourceLoader resourceLoader;
    @Value("${pard.fileUploadPath}")
    private String ROOT = "/Users/wawe/upfile/";

    @Autowired
    public FileUploadController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @RequestMapping(value = "/upload/{filename:.+}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String filename) {
        try {
            logger.info(filename);
            return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(ROOT, filename).toString()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/upfile1/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage handleFileUpload(@RequestParam("file") MultipartFile... files) {
        List<String> fileNames = Lists.newArrayList();
        if (files.length > 0) {
            for (MultipartFile file : files) {
                if (file.getSize() > 0) {
                    try {
                        fileNames.add(Paths.get(ROOT, file.getOriginalFilename()).toString());
                        Files.copy(file.getInputStream(), Paths.get(ROOT, file.getOriginalFilename()));
                    } catch (IOException | RuntimeException e) {
                        return ResponseMessage.error("Failed to upload " + file.getOriginalFilename() + " =>" + e.getMessage());
                    }
                }
            }
            return ResponseMessage.ok(fileNames);
        } else {
            return ResponseMessage.error("Failed to upload, because it was empty");
        }
    }

    @RequestMapping(value = "/upfile/image/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage handleImageUpload(@RequestParam(value = "file") MultipartFile... files) {
        if (files.length > 0) {
            User user = UserUtils.getUser();
            StringBuilder sbPath = new StringBuilder();
            String relativePath = user.getLoginName() + "/image/" + DateTimeUtils.format(new Date(), DateTimeUtils.YEAR_MONTH_DAY_SIMPLE);
            sbPath.append(ROOT.charAt(ROOT.length() - 1) == '/' ? ROOT.substring(0, ROOT.length() - 1) : ROOT)
                    .append("/").append(relativePath);
            List<String> fileNames = Lists.newArrayList();
            for (MultipartFile file : files) {
                if (file.getSize() > 0) {
                    try {
                        String fileName = file.getOriginalFilename();
                        String suffixName = fileName.substring(fileName.lastIndexOf("."));
                        String newFileName = DateTimeUtils.currentTimeMillis() + "" +
                                RandomUtils.nextInt(100000, 999999) + suffixName;
                        String fileUrl = "/upload/" + relativePath + "/" + newFileName;
                        fileNames.add(fileUrl);
                        File dest = Paths.get(sbPath.toString(), newFileName).toFile();
                        if (!dest.getParentFile().exists()) {
                            dest.getParentFile().mkdirs();
                        }
                        Files.copy(file.getInputStream(), Paths.get(sbPath.toString(), newFileName));
                    } catch (IOException | RuntimeException e) {
                        return ResponseMessage.error("上传文件 " + file.getOriginalFilename() + " 失败，文件处理异常，稍后重试 =>" + e.getMessage());
                    }
                }
            }
            return ResponseMessage.ok(fileNames);
        }
        return ResponseMessage.error("上传失败，文件为空");
    }
}
