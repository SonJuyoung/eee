package ieetu.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultDto implements Serializable {
    private String method;
    private String action;
    /* 생성자 */
    private Integer createSeq;
    /* 생성일 */
    private String createDate;
    /* 수정자 */
    private Integer updateSeq;
    /* 수정일 */
    private String updateDate;
}
