package ieetu.common.file.enums;

public enum FileType {
    PNG("png"),
    JPG("jpg"),
    JPEG("jpeg"),
    GIF("gif"),
    BMP("bmp"),
    AVI("avi"),
    WMV("wmv"),
    MP4("mp4"),
    WEBM("webm"),
    PDF("pdf"),
    XLS("xls"),
    XLSX("xlsx"),
    DOC("doc"),
    DOCX("docx"),
    PPT("ppt"),
    PPTX("pptx"),
    HWP("hwp");

    private final String type;

    FileType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
