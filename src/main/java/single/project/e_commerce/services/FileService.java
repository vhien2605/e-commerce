package single.project.e_commerce.services;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileService {
    private final Cloudinary cloudinary;

    public String uploadFile(MultipartFile file, String folderName) throws IOException {
        Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", folderName
                ));
        return result.get("secure_url").toString();
    }


    public void deleteFileByUrl(String imageUrl) throws IOException {
        String publicId = extractPublicIdFromUrl(imageUrl);
        if (publicId == null) {
            return;
        }
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
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
