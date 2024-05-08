package com.shoppingmall.orderservice.dto;

import jakarta.validation.constraints.NotNull;
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
public class ChangePasswordDto {

    @NotNull
    private String password;
}