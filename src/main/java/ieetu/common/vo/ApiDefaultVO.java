package ieetu.common.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ApiDefaultVO implements Serializable {
    /* 생성자 */
    private Long createdBy;
    /* 생성일 */
    private LocalDateTime createdDate;
    /* 수정자 */
    private Long lastModifiedBy;
    /* 수정일 */
    private LocalDateTime lastModifiedDate;
}
