package com.shoppingmall.orderservice.dto;

import java.util.List;
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
public class SelectWishListDto {
    long userId;
    List<Long> selectlist;
}
