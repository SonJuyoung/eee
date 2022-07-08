package ieetu.common.dto;

import lombok.Data;

@Data
public class FileDto {

    private int iboard;
    private String fileNm;
    private String uploadPath;
    private String uuid;
}
