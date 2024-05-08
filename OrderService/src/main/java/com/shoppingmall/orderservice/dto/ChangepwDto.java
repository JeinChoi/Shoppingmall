package com.shoppingmall.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import java.sql.Timestamp;
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