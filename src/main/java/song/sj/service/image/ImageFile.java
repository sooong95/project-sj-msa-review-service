package song.sj.service.image;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import song.sj.entity.BaseImages;
import song.sj.entity.ReviewImages;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class ImageFile {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    public <T extends BaseImages> T serverFile(MultipartFile file, Class<T> clazz) throws IOException {

        if (file.isEmpty()) {
            throw new RuntimeException("이미지를 등록해 주세요");
        }

        String originalFilename = file.getOriginalFilename();
        String serverFileName = createServerFileName(originalFilename);
        String contentType = file.getContentType();

        try {
            file.transferTo(new File(getFullPath(serverFileName)));
        } catch (IOException e) {
            log.info("serverFile error={}", e.getMessage());
        }

        if (clazz.equals(ReviewImages.class)) {
            return clazz.cast(new ReviewImages(originalFilename, serverFileName, contentType));
        }
        throw new IllegalArgumentException("존재하지 않는 이미지 타입");
    }

    private String createServerFileName(String originalFilename) {

        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
