package ieetu.common.dto;

import lombok.Data;

@Data
public class BoardDto {

    private int iboard;
    private String title;
    private String writer;
    private String ctnt;
    private String rdt;
    private int fix;
    private String file;
    private int iuser;
}
