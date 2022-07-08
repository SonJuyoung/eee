package ieetu.common.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "file")
@Data
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ifile;

    private String fileNm;

    private int iboard;
}
