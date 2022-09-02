package ieetu.common.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int iuser;

    @Column(unique = true, nullable = false)
    private String uid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String upw;

    @Column(unique = true, nullable = false)
    private String mail;

    @Column(unique = true, nullable = false)
    private String phone;

    private String img;

    @Column(nullable = false)
    private String address;

    @Column
    private String addressDetail;

    @Column
    private String addressExtra;

    @Column(nullable = false)
    private int postcode;

    public UserEntity(int iuser) {
        this.iuser = iuser;
    }

    public void changeUpw(String upw) {
        this.upw = upw;
    }

}