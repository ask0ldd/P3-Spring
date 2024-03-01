package com.example.immo.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    @Value("${file.path}")
    private String filePath;

    // should check if file not already exists
    // check if jpg / png
    public String save(MultipartFile file) {
        // String dir = System.getProperty("user.dir") + "/" + filePath;
        String dir = System.getProperty("user.dir") + "/src/main/resources/static/" + filePath;
        try {
            // file.transferTo(new File(dir + "/" + file.getOriginalFilename()));
            String filename = generateUniqueFileName("img-", ".jpg");
            // file.transferTo(new File(dir + "/" + file.getOriginalFilename()));
            // file.transferTo(new File(dir + "/" + filename));
            file.transferTo(new File(dir + filename));
            return filename;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public InputStream getResource(String fileName) throws IOException {
        // String IMAGE_FOLDER = System.getProperty("user.dir") + "/" + filePath;
        // String fullPath = IMAGE_FOLDER + "/" + fileName;
        var imgFile = new ClassPathResource("static/uploads/" + fileName);
        return imgFile.getInputStream();
        // return new FileInputStream(fullPath);
    }

    public Resource getResource2(String fileName) throws IOException {
        String IMAGE_FOLDER = System.getProperty("user.dir") + "/" + filePath;
        Path imagePath = Paths.get(IMAGE_FOLDER).resolve(fileName);
        Resource resource = new UrlResource(imagePath.toUri());
        return resource;
    }

    public static String generateUniqueFileName(String prefix, String suffix) {
        return (prefix != null ? prefix : "") + System.nanoTime() + (suffix != null ? suffix : "");
    }
}
