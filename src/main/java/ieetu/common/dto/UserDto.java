package ieetu.common.dto;

import lombok.Data;

@Data
public class UserDto {

    private String id;
    private String name;
    private String pw;
    private String mail;
    private int phone;
}
