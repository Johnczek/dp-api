package cz.johnczek.dpapi.file.service;

import cz.johnczek.dpapi.core.errorhandling.exception.FileNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.FileStorageInternalErrorRestException;
import cz.johnczek.dpapi.file.entity.FileEntity;
import cz.johnczek.dpapi.file.enums.FileType;
import lombok.NonNull;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface FileService {

    /**
     * Method stores physically file in file system in structure by given file type and creates corresponding
     * {@link FileEntity} record in database.
     *
     * @param multipartFile data of file we want to store
     * @param fileType type of file
     * @return UUID identifier of new file if whole process was successful, empty optional otherwise
     * @throws FileStorageInternalErrorRestException in case that file could not be stored physically in file system
     */
    Optional<String> storeFile(@NonNull MultipartFile multipartFile, @NonNull FileType fileType);

    /**
     * Method retrieves file from file system by given UUID identifier.
     *
     * @param fileUUID uuid of file we want to find
     * @return file data, empty optional if file could not be retrieved
     * @throws FileNotFoundRestException in case that file could not be found in system (in database or physically in file system)
     */
    Optional<Resource> loadFile(@NonNull String fileUUID);

    /**
     * @param uuid uuid of file we want to find
     * @return file entity if retrieval was successful, empty optional otherwise
     */
    Optional<FileEntity> findByFileIdentifier(@NonNull String uuid);
}
