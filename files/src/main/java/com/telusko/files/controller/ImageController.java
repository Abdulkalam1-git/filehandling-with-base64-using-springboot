package com.telusko.files.controller;

import com.telusko.files.Model.ImageFile;
import com.telusko.files.service.ImageFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageFileService imageService;

    @Autowired
    public ImageController(ImageFileService imageService) {
        this.imageService = imageService;

    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return new ResponseEntity<>("Please select a valid image file to upload", HttpStatus.BAD_REQUEST);
            }

            // Process and save the image file
            ImageFile savedImage = imageService.processAndSaveFile(file);
            return new ResponseEntity<>("Image uploaded and saved successfully: " + savedImage.getFilePath(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Image upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<List<ImageFile>> getAllImages() {
        return imageService.getFiles();
    }
    @GetMapping("/{Filename}")
    public ResponseEntity<List<ImageFile>> getImage(@PathVariable String Filename ) {
        return imageService.getFileByName(Filename);
    }
    @GetMapping("/{Filetype}")
    public ResponseEntity<List<ImageFile>> getImageByFiletype(@PathVariable String Filetype) {
        return imageService.getFileByType(Filetype);
    }



}
