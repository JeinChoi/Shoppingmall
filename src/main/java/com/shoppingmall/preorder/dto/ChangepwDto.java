package com.shoppingmall.preorder.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangepwDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String password;

}