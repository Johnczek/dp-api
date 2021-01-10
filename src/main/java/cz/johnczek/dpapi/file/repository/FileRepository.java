package cz.johnczek.dpapi.file.repository;

import cz.johnczek.dpapi.file.entity.FileEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    boolean existsByFilePath(@NonNull String filePath);

    Optional<FileEntity> findByFilePath(@NonNull String filePath);

}
