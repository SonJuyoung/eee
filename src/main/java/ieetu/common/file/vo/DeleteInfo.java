package ieetu.common.file.vo;

import ieetu.common.file.entity.RefType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class DeleteInfo {
    List<Long> deleteList;
    @NotBlank
    RefType refType;
    @NotBlank
    Long refSeq;
    Long mbrSeq;
    public boolean isValid() {
        return deleteList.size() != 0 && refType != null && refSeq != null && mbrSeq != null;
    }
}
