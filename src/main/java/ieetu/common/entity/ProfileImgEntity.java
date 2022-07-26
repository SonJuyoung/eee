package ieetu.common.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "profileimg")
@Data
public class ProfileImgEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false)
    private int idx;

    private String fileNm;

    @OneToOne
    @JoinColumn(name = "iuser")
    private UserEntity iuser;
}