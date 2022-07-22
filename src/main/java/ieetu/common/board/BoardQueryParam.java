package ieetu.common.board;

import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
public class BoardQueryParam {
    private String search;
    private Pageable pageable;
    private String startDate;
    private String endDate;
}
