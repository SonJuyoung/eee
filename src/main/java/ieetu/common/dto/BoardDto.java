package ieetu.common.dto;

import lombok.Data;

@Data
public class BoardDto {

    private String title;
    private String writer;
    private String ctnt;
    private String rdt;
    private int fix;
}
