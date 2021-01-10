package cz.johnczek.dpapi.file.service;

import cz.johnczek.dpapi.file.enums.FileType;
import lombok.NonNull;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface FileService {

    Optional<String> storeFile(@NonNull MultipartFile multipartFile, @NonNull FileType fileType);

    Optional<Resource> loadFile(@NonNull String fileUUID);
}
