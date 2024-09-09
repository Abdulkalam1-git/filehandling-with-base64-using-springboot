package com.telusko.files;


// Response DTO for Upload Image
public class UploadImageResponse {
    private String message;
    private String filePath;

    public UploadImageResponse(String message, String filePath) {
        this.message = message;
        this.filePath = filePath;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
