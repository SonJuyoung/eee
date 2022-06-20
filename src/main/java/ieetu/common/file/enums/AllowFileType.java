package ieetu.common.file.enums;

import java.util.Arrays;
import java.util.List;

public enum AllowFileType {
    ALL("제한없음", null),
    IMAGE("이미지", Arrays.asList(FileType.PNG, FileType.JPG, FileType.JPEG, FileType.GIF, FileType.BMP)),
    VIDEO("비디오", Arrays.asList(FileType.AVI, FileType.WMV, FileType.MP4, FileType.WEBM)),
    WORD("워드", Arrays.asList(FileType.DOC, FileType.DOCX)),
    PPT("파워포인트", Arrays.asList(FileType.PPT, FileType.PPTX)),
    EXCEL("엑셀", Arrays.asList(FileType.XLS, FileType.XLSX)),
    OFFICE("오피스", Arrays.asList(FileType.PDF, FileType.HWP, FileType.XLS, FileType.XLSX, FileType.PPT, FileType.PPTX, FileType.DOC, FileType.DOCX))
    ;


    private String title;
    private List<FileType> allowList;

    AllowFileType(String title, List<FileType> allowList) {
        this.title = title;
        this.allowList = allowList;
    }

    public String getTitle() {
        return title;
    }

    public boolean isValid(String ext) {
        if(title.equals("제한없음") && allowList == null) {
            return true;
        }

        for(FileType type : allowList) {
            if(ext.equalsIgnoreCase(type.getType())) {
                return true;
            }
        }

        return false;
    }

}
