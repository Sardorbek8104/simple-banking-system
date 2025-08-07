package uz.sardor.simplebankingsystem.dto.auth.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private long id;
    private String name;
    private String email;
}
