package single.project.e_commerce.services;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncFileService {

    @Async
    public void handleUploadFile(MultipartFile file, String folderName, String uuid, Cloudinary cloudinary) throws IOException {
        log.info("Thread_upload: {}", Thread.currentThread().getName());
        cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", folderName,
                        "public_id", uuid
                ));
    }

    @Async
    public void handleDeleteFile(String publicId, Cloudinary cloudinary) throws IOException {
        log.info("Thread_delete: {}", Thread.currentThread().getName());
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
