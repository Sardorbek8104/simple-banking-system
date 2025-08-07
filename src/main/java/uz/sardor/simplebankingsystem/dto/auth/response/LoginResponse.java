package uz.sardor.simplebankingsystem.dto.auth.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse {
    private String accessToken;
    private String refreshToken;

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
