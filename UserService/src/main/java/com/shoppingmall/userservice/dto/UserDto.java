package com.shoppingmall.userservice.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @NotNull
    @Size(min = 3, max = 50)
    private String phoneNumber;

    @NotNull
    @Size(min = 3, max = 50)
    private String email;

    @NotNull
    @Size(min = 3, max = 50)
    private String city;

    @NotNull
    @Size(min = 3, max = 50)
    private String street;

    @NotNull
    @Size(min = 3, max = 50)
    private String zipcode;

    private String email_authentication_token;

    public void updateEamilAuthenticationToken(String email_authentication_token){
        this.email_authentication_token=email_authentication_token;
    }
}
