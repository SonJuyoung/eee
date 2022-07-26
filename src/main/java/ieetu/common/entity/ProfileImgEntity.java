package ieetu.common.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "profileimg")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
