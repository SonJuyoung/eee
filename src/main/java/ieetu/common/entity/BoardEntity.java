package ieetu.common.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "board")
@Getter
@Builder
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int iboard;

    private String writer;

    private String title;

    private String ctnt;

    private String rdt;

    private int fix;

    @ManyToOne
    @JoinColumn(name = "iuser")
    private UserEntity iuser;

    public BoardEntity() {
    }

    public void changeIboard(int iboard) {
        this.iboard = iboard;
    }

    public void changeBoard(String title, String ctnt, int fix) {
        this.title = title;
        this.ctnt = ctnt;
        this.fix = fix;
    }
}
