package ieetu.common.dto;

import lombok.Data;

@Data
public class UserDto {

    private String id;
    private String name;
    private String pw;
    private String mail;
    private String phone;
    private String img;
    private int iuser;
    private int postcode;
    private String address;
    private String addressDetail;
    private String addressExtra;
}
