package ieetu.common.file.entity;

import ieetu.common.entity.BaseEntity;
import ieetu.common.file.enums.FileAuth;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.io.File;


@Entity
@Getter
@Table(name = "tb_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FileEntity extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "file_seq")
    @Comment("파일 시퀀스")
    private Long seq;

    @Column
    private Long refSeq;

    /**
     * 구분자
     */
    @Enumerated(EnumType.STRING)
    private RefType refType;

    @Column
    private String originalFileNm;

    @Column
    private String storeFileNm;

    @Column
    private String filePath;

    @Column
    private Long fileSize;

    /**
     * 다운로드 권한
     */
    @Enumerated(EnumType.STRING)
    private FileAuth downloadAuth;

    /**
     * 삭제 권한
     */
    @Enumerated(EnumType.STRING)
    private FileAuth deleteAuth;

    public String getAbsolutePath() {
        return filePath + File.separator + storeFileNm;
    }
}
