package shoppingmall.hanaro.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        // yyyy/MM/dd 형식의 날짜 폴더 경로 생성
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String uploadPath = Paths.get(uploadDir, datePath).toString();

        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 파일명 중복 방지를 위한 UUID 사용
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + extension;

        // 파일 저장
        File targetFile = new File(Paths.get(uploadPath, newFilename).toString());
        file.transferTo(targetFile);

        // 저장된 파일의 상대 경로 반환 (예: /upload/2025/07/25/uuid.jpg)
        return Paths.get("/upload", datePath, newFilename).toString().replace("\\", "/");
    }
}
