package com.shoppingmall.productservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

public enum ItemState {
    ON_SALE,SOLD_OUT
}
