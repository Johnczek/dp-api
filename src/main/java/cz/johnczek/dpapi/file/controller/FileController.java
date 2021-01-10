package cz.johnczek.dpapi.file.controller;

import cz.johnczek.dpapi.core.errorhandling.exception.FileNotFoundRestException;
import cz.johnczek.dpapi.file.enums.FileType;
import cz.johnczek.dpapi.file.response.FileUploadResponse;
import cz.johnczek.dpapi.file.service.FileService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file,
                                                        @RequestParam(required = false) FileType fileType) {

        if (fileType == null) {
            fileType = FileType.UNKNOWN;
        }

        Optional<String> fileUUID = fileService.storeFile(file, fileType);

        return fileUUID
                .map(s -> new ResponseEntity<>(new FileUploadResponse(s), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Resource> downloadFile(@PathVariable @NonNull String uuid, ServletRequest request) {

        Resource resource = fileService.loadFile(uuid).orElseThrow(() -> {
            log.error("File with UUID {} could not be retrieved", uuid);

            return new FileNotFoundRestException(uuid);
        });

        String contentType = "application/octet-stream";
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.warn("Content type for file with uuid {} could not be determined", uuid);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
