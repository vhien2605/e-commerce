package single.project.e_commerce.services;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final Cloudinary cloudinary;
    private final AsyncFileService asyncFileService;

    public String uploadFile(MultipartFile file, String folderName) throws IOException {
        String cloudName = cloudinary.config.cloudName;
        String uuid = UUID.randomUUID().toString();
        String extension = getFileExtension(file);
        String url = String.format(
                "https://res.cloudinary.com/%s/image/upload/%s/%s.%s",
                cloudName, folderName, uuid, extension
        );
        // handle upload to cloud in sub thread
        asyncFileService.handleUploadFile(file, folderName, uuid, cloudinary);
        return url;
    }


    public void deleteFileByUrl(String imageUrl) throws IOException {
        String publicId = extractPublicIdFromUrl(imageUrl);
        if (publicId == null) {
            return;
        }
        asyncFileService.handleDeleteFile(publicId, cloudinary);
    }

    private String getFileExtension(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.contains(".")) {
            return "jpg";
        }
        return originalName.substring(originalName.lastIndexOf('.') + 1);
    }


    private String extractPublicIdFromUrl(String imageUrl) {
        try {
            String baseUrl = "https://res.cloudinary.com/";
            if (!imageUrl.startsWith(baseUrl)) return null;
            // Cắt từ phần sau 'upload/' tới trước đuôi .jpg/.png...
            String[] parts = imageUrl.split("/upload/");
            if (parts.length < 2) return null;
            String path = parts[1];
            int dotIndex = path.lastIndexOf('.');
            if (dotIndex != -1) {
                path = path.substring(0, dotIndex);
            }
            return path;
        } catch (Exception e) {
            return null;
        }
    }
}
