package ieetu.common.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int iuser;

    @Column(unique = true)
    private String uid;

    private String name;

    private String upw;

    private String mail;

    private int phone;
}