package ieetu.common.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "file")
@Getter
@NoArgsConstructor
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ifile;

    private String fileNm;

    //jpa 다대일 외래키 설정
    @ManyToOne
    @JoinColumn(name = "iboard")
    @OnDelete(action = OnDeleteAction.CASCADE) //board 테이블에서 삭제 되었을 때 해당 iboard를 가진 file 테이블 튜플 삭제
    private BoardEntity iboard;

    public FileEntity(BoardEntity iboard, String fileNm) {
        this.iboard = iboard;
        this.fileNm = fileNm;
    }
}
