package ieetu.common.file.dto;

import ieetu.common.file.entity.FileEntity;
import ieetu.common.file.entity.RefType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
   private Long fileSeq;
   private Long refSeq;
   private String storeFileNm;
   private RefType refType;
   private String originalFileNm;
   private Long fileSize;

   public static FileDto fromEntity(FileEntity file) {
      return FileDto.builder()
              .fileSeq(file.getSeq())
              .refSeq(file.getRefSeq())
              .refType(file.getRefType())
              .storeFileNm(file.getStoreFileNm())
              .originalFileNm(file.getOriginalFileNm())
              .fileSize(file.getFileSize())
              .build();
   }
}
