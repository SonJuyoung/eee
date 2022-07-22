package ieetu.common.entity;

import lombok.Data;
import org.apache.ibatis.annotations.Update;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Data
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imsg;

    private int room;

    @ManyToOne
    @JoinColumn(name = "iuser")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity sendUser;

    @ManyToOne
    @JoinColumn(name = "iuser")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity recvUser;

    @Column(insertable = false, updatable = false)
    private LocalDateTime sendTime;

    @Column(insertable = false, updatable = false)
    private LocalDateTime readTime;

    private String ctnt;

    private int readChk;
}
