package ieetu.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "board")
@Data
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int iboard;

    private String writer;

    private String title;

    private String ctnt;

    private String rdt;

    private String file;

    private int fix;
}
