package ieetu.common.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "board")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    private int view;

    public BoardEntity(int iboard) {
        this.iboard = iboard;
    }
}
