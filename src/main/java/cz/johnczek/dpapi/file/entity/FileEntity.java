package cz.johnczek.dpapi.file.entity;

import cz.johnczek.dpapi.core.persistence.AbstractIdBasedEntity;
import cz.johnczek.dpapi.file.enums.FileType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "file", schema = "public")
public class FileEntity extends AbstractIdBasedEntity<Long> {

    private String filePath;

    @Enumerated(value = EnumType.STRING)
    private FileType type;
}
