package com.telusko.files.service;

import com.telusko.files.Model.ImageFile;
import com.telusko.files.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

@Service
public class ImageFileService {

    private final ImageRepository imageRepository;

    // Directories for storing images and PDFs
    private final String imageDir = "C:\\Users\\abdul\\OneDrive\\ecommerce";
    private final String pdfDir = "C:\\Users\\abdul\\Downloads\\filehandling";

    @Autowired
    public ImageFileService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    // Process file- convert to Base64, decode, save to file system, and store info in DB
    public ImageFile processAndSaveFile(MultipartFile file) throws IOException {
        // Determine MIME type and choose the correct directory
        String fileType = file.getContentType();
        String uploadDir;

        if (fileType != null && fileType.equals("application/pdf")) {
            uploadDir = pdfDir;  // Store in PDF directory
        } else if (fileType != null && (fileType.equals("image/png") || fileType.equals("image/jpeg"))) {
            uploadDir = imageDir; // Store in Image directory
        } else {
            throw new IOException("Unsupported file type: " + fileType);
        }

        // Convert file to Base64
        String base64File = convertToBase64(file);

        // Decode Base64 and save file to file system
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String filePath = Paths.get(uploadDir, fileName).toString();

        // Check if file already exists
        if (fileExists(filePath)) {
            throw new IOException("File with name " + fileName + " already exists.");
        }

        // Save file to file system
        saveFileFromBase64(base64File, filePath);

        // Create and save ImageFile entity (you might want to rename this if handling more than images)
        return saveFileEntity(fileName, filePath, fileType, file.getSize());
    }

    // Convert MultipartFile to Base64 string
    private String convertToBase64(MultipartFile file) throws IOException {
        byte[] fileContent = file.getBytes();
        return Base64.getEncoder().encodeToString(fileContent);
    }

    // Decode Base64 string and save file to the file system
    private void saveFileFromBase64(String base64File, String filePath) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(base64File);
        try (FileOutputStream fos = new FileOutputStream(new File(filePath))) {
            fos.write(decodedBytes);
        }
    }

    // Create and save ImageFile (or general File entity)
    private ImageFile saveFileEntity(String fileName, String filePath, String fileType, long size) {
        ImageFile file = new ImageFile(fileName, filePath, fileType, size);
        return imageRepository.save(file);
    }

    // Check if file already exists
    private boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    // Retrieve all files
    public ResponseEntity<List<ImageFile>> getFiles() {
        List<ImageFile> files = imageRepository.findAll();
        return ResponseEntity.ok(files);
    }

    // Retrieve file by filename
    public ResponseEntity<List<ImageFile>> getFileByName(String fileName) {
        List<ImageFile> files = imageRepository.findByFileName(fileName);
        return ResponseEntity.ok(files);
    }

    // Retrieve file by path
    public ResponseEntity<List<ImageFile>> getFileByType(String type) {
        List<ImageFile> files = imageRepository.findByFileType(type);
        return ResponseEntity.ok(files);
    }

}
