package ieetu.common.file.vo;

import ieetu.common.file.entity.RefType;
import ieetu.common.file.enums.FileAuth;
import ieetu.common.vo.ApiDefaultVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileVO extends ApiDefaultVO {
    private Long fileSeq;

    private Long refSeq;

    private RefType refType;

    private String originalFileNm;

    private String storeFileNm;

    private String filePath;

    private Long fileSize;

    private FileAuth downloadAuth;

    private FileAuth deleteAuth;

    public String getAbsolutePath() {
        return filePath + File.separator + storeFileNm;
    }

}