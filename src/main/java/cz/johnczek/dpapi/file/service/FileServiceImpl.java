package cz.johnczek.dpapi.file.service;

import cz.johnczek.dpapi.core.errorhandling.exception.FileNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.FileStorageInternalErrorRestException;
import cz.johnczek.dpapi.file.configuration.FileStorageProperties;
import cz.johnczek.dpapi.file.entity.FileEntity;
import cz.johnczek.dpapi.file.enums.FileType;
import cz.johnczek.dpapi.file.repository.FileRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private Path fileLocation;

    private final FileStorageProperties fileStorageProperties;

    private final FileRepository fileRepository;

    @Override
    public Optional<String> storeFile(@NonNull MultipartFile multipartFile, @NonNull FileType fileType) {

        String originalFileName = multipartFile.getOriginalFilename();
        if (ObjectUtils.isEmpty(originalFileName)) {
            log.error("Error uploading image. Multipart file original filename is null or empty: {}",
                    multipartFile.getOriginalFilename());
            throw new FileStorageInternalErrorRestException();
        }

        UUID uuid = UUID.randomUUID();

        try {

            MimeType type = MimeTypes.getDefaultMimeTypes().forName(multipartFile.getContentType());
            String extension = type.getExtension();

            Path targetDirectory = getFileLocation().resolve(fileType.getFolder());
            Files.createDirectories(targetDirectory);

            Path target = targetDirectory.resolve(uuid.toString() + extension);
            Files.copy(multipartFile.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            fileRepository.save(prepare(uuid.toString(), extension.substring(1), fileType));

        } catch (IOException e) {
            log.error("Error uploading image. File {} could not be stored", multipartFile.getOriginalFilename(), e);

            throw new FileStorageInternalErrorRestException();
        } catch (MimeTypeException e) {
            log.error("Error getting file extension for file with content type {}", multipartFile.getContentType(), e);

            throw new FileStorageInternalErrorRestException();
        }

        return Optional.ofNullable(uuid.toString());
    }

    public Optional<Resource> loadFile(@NonNull String fileUUID) {

        FileEntity fileEntity = fileRepository.findByFileIdentifier(fileUUID).orElseThrow(() -> {
            log.warn("File with UUID {} not found in database", fileUUID);

            return new FileNotFoundRestException(fileUUID);
        });

        try {
            Path filePath = getFileLocation()
                    .resolve(fileEntity.getType().getFolder())
                    .resolve(fileEntity.getFileIdentifier() + '.' + fileEntity.getFileExtension());

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return Optional.of(resource);
            } else {
                log.warn("File with UUID {} was not found in filesystem", fileUUID);

                throw new FileNotFoundRestException(fileUUID);
            }

        } catch (MalformedURLException e) {
            log.warn("Unable to retrieve File with UUID {} from filesystem", fileUUID);

            throw new FileNotFoundRestException(fileUUID);
        }
    }

    @Override
    public Optional<FileEntity> findByFileIdentifier(@NonNull String uuid) {
        return fileRepository.findByFileIdentifier(uuid);
    }

    private FileEntity prepare(@NonNull String identifier, @NonNull String extension, @NonNull FileType fileType) {
        FileEntity result = new FileEntity();
        result.setFileIdentifier(identifier);
        result.setFileExtension(extension);
        result.setType(fileType);

        return result;
    }

    private Path getFileLocation() {
        if (fileLocation != null) {
            return fileLocation;
        }

        fileLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileLocation);
        } catch (IOException e) {
            log.error("Error accessing directory for file upload {}", fileLocation, e);

            throw new FileStorageInternalErrorRestException();
        }

        return fileLocation;
    }
}
