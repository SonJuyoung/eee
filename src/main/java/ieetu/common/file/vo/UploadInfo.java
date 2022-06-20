package ieetu.common.file.vo;

import ieetu.common.file.entity.RefType;
import ieetu.common.file.enums.AllowFileType;
import ieetu.common.file.enums.FileAuth;
import ieetu.common.utils.PropertiesLoader;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.util.List;

@Data
public class UploadInfo {
    private List<MultipartFile> files;
    @NotBlank
    private RefType refType;
    @NotBlank
    private Long refSeq;

//    @Value("${spring.servlet.multipart.location}")
    private String rootPath = PropertiesLoader.getProperties("spring.servlet.multipart.location").toString();

    /**
     * 기본 파일 경로
     */
    //String rootPath = EgovProperties.getProperty("Globals.file.path");
    private FileAuth downloadAuth = FileAuth.ALL;
    private FileAuth deleteAuth = FileAuth.OWNER;

    private AllowFileType allowFileType = AllowFileType.ALL;

    private Long createdBy;

    public boolean isValid() {
        return files.size() != 0 && refType != null && refSeq != null && rootPath != null && downloadAuth != null && deleteAuth != null;
    }

    public String getAbsoluteFilePath() {
        if (!rootPath.endsWith(File.separator)) {
            rootPath += File.separator;
        }
        return rootPath + refType.name() + File.separator + refSeq + File.separator;
    }
}
