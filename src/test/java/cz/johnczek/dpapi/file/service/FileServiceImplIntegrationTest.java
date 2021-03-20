package cz.johnczek.dpapi.file.service;

import cz.johnczek.dpapi.AbstractIntegrationTest;
import cz.johnczek.dpapi.core.errorhandling.exception.FileNotFoundRestException;
import cz.johnczek.dpapi.file.entity.FileEntity;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileServiceImplIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private FileServiceImpl instance;

    @Nested
    class LoadFile {

        @Test
        void notExistingFile_exceptionThrown() {

            assertThrows(FileNotFoundRestException.class,
                    () -> instance.loadFile("UUID")
            );
        }
    }

    @Nested
    class FindByFileIdentifier {

        @Test
        void notExistingFile_exceptionThrown() {
            assertThat(instance.findByFileIdentifier("UUID")).isEmpty();
        }

        @Test
        void existingFile_entityReturned() {

            String fileUUID = "ce9222c6-58d9-11eb-ae93-0242ac130002";

            Optional<FileEntity> result = instance.findByFileIdentifier(fileUUID);

            assertThat(result).isNotEmpty();
            assertThat(result.get().getFileIdentifier()).isEqualTo(fileUUID);
        }
    }
}