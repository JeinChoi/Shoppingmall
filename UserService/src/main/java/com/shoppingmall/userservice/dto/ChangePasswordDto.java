package com.shoppingmall.userservice.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {

    private Long userId;

    @NotNull
    private String password;
}
