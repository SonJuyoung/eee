package ieetu.common.file.repository;

import ieetu.common.file.entity.FileEntity;
import ieetu.common.file.entity.RefType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByRefSeqAndRefType(Long refSeq, RefType refType);
    List<FileEntity> findBySeqIn(List<Long> deleteSeqList);
}
