package cz.johnczek.dpapi.file.controller;

import cz.johnczek.dpapi.core.errorhandling.exception.FileNotFoundRestException;
import cz.johnczek.dpapi.file.enums.FileType;
import cz.johnczek.dpapi.file.response.FileUploadResponse;
import cz.johnczek.dpapi.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(summary = "Method uploads file", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "UUID identifier of newly uploaded image is returned")
    @ApiResponse(responseCode = "400", description = "In case of generic error not listed below")
    @ApiResponse(responseCode = "403", description = "In case that user is not logged in")
    @ApiResponse(responseCode = "500", description = "In case of internal error while saving file to filesystem")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FileUploadResponse> uploadFile(@Parameter(description="File content in blob format") @RequestParam("file") MultipartFile file,
                                                         @Parameter(description="Parameter for specifying file (avatar, item photo, ...)") @RequestParam(required = false) FileType fileType) {

        if (fileType == null) {
            fileType = FileType.UNKNOWN;
        }

        Optional<String> fileUUID = fileService.storeFile(file, fileType);

        return fileUUID
                .map(s -> new ResponseEntity<>(new FileUploadResponse(s), HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

    @Operation(summary = "Method returns file by given UUID")
    @ApiResponse(responseCode = "200", description = "File by given identifier is returned")
    @ApiResponse(responseCode = "404", description = "In case that file was not found")
    @ApiResponse(responseCode = "500", description = "In case of internal error while retrieving file from filesystem")
    @GetMapping("/{uuid}")
    public ResponseEntity<Resource> downloadFile(@Parameter(description="UUID identifier of file we want to retrieve")
                                                     @PathVariable String uuid, ServletRequest request) {

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
