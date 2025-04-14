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
}
