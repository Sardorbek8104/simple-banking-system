package uz.sardor.simplebankingsystem.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserUpdateRequest {
    private String name;
    private String email;
    private String password;
    private String role;
    private String status;
}
