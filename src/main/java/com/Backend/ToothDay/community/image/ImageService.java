package com.Backend.ToothDay.community.image;

import com.Backend.ToothDay.community.post.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;

    public void saveImages(Post post, List<MultipartFile> files)  {
        //String uploadDir = "src/main/resources/static/upload";
        String uploadDir = System.getProperty("user.dir") + File.separator + "upload" + File.separator;

        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            File dest =new File(uploadDir + fileName);

            try {
                file.transferTo(dest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Image image = new Image(post,fileName);
            imageRepository.save(image);
            post.getImageList().add(image);
        }
    }

    public void deleteAllByPostId(long postId) {
        imageRepository.deleteAllByPostId(postId);
    }




}
