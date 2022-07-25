package ieetu.common.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "reply")
@Getter
@Builder
public class ReplyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ireply;

    private String ctnt;

    //jpa 다대일 외래키 설정
    @ManyToOne
    @JoinColumn(name = "iboard")
    @OnDelete(action = OnDeleteAction.CASCADE) //board 테이블에서 삭제 되었을 때 해당 iboard를 가진 reply 테이블 튜플 삭제
    private BoardEntity iboard;

    //jpa 다대일 외래키 설정
    @ManyToOne
    @JoinColumn(name = "iuser")
    @OnDelete(action = OnDeleteAction.CASCADE) //user 테이블에서 삭제 되었을 때 해당 iuser를 가진 reply 테이블 튜플 삭제
    private UserEntity iuser;

    private String name;

    public ReplyEntity() {

    }
}
