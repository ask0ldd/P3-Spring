package com.example.immo.controllers;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.immo.services.FileService;

import jakarta.servlet.http.HttpServletResponse;

// 127.0.0.1:3001/img/rental/griff.jpg
@RestController
@RequestMapping("img")
@CrossOrigin(origins = "http://localhost:4200")
public class ImageController {

    private final FileService fileService;

    public ImageController(FileService fileService) {
        this.fileService = fileService;
    }

    @Value("${file.path}")
    private String filePath;
    // private static final String IMAGE_FOLDER = "/uploads/";

    /*
     * @GetMapping("/images/{imageName}")
     * public ResponseEntity<Resource> serveImage(@PathVariable String imageName) {
     * try {
     * Resource resource = fileService.getResource2(imageName);
     * 
     * if (resource.exists() || resource.isReadable()) {
     * return ResponseEntity.ok()
     * .header(HttpHeaders.CONTENT_TYPE, "image/jpeg") // Change content type based
     * on your image type
     * .body(resource);
     * } else {
     * return ResponseEntity.notFound().build();
     * }
     * } catch (IOException e) {
     * return ResponseEntity.notFound().build();
     * }
     * }
     * 
     * @GetMapping("/getimage")
     * 
     * @ResponseBody
     * public ResponseEntity<InputStreamResource>
     * getImageDynamicType(@RequestParam("jpg") boolean jpg) {
     * String IMAGE_FOLDER = System.getProperty("user.dir") + "/" + filePath;
     * MediaType contentType = jpg ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG;
     * InputStream in = jpg ? getClass().getResourceAsStream(IMAGE_FOLDER +
     * "griff.jpg")
     * : getClass().getResourceAsStream(IMAGE_FOLDER + "griff.png");
     * return ResponseEntity.ok()
     * .contentType(contentType)
     * .body(new InputStreamResource(in));
     * }
     */

    @GetMapping(value = "/rental/{imageName}", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    public void getImage(HttpServletResponse response, @PathVariable String imageName) throws IOException {
        InputStream stream = fileService.getResource(imageName);
        StreamUtils.copy(stream, response.getOutputStream());
    }
}
