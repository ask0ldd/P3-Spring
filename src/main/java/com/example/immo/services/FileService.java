package com.example.immo.services;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    @Value("${file.path}")
    private String filePath;

    // should check if file not already exists
    public String save(MultipartFile file) {
        String dir = System.getProperty("user.dir") + "/" + filePath;
        try {
            file.transferTo(new File(dir + "/" + file.getOriginalFilename()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return dir;
    }
}
