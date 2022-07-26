package ieetu.common.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter
@Builder
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

    @Column(nullable = false)
    private int postcode;

    public UserEntity() {

    }

    public void changePw(String upw) {
        this.upw = upw;
    }

    public void changeIuser(int iuser) {
        this.iuser = iuser;
    }

    public void changeUser(String upw, String address, int postcode) {
        this.upw = upw;
        this.address = address;
        this.postcode = postcode;
    }
}