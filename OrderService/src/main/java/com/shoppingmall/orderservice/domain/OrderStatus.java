package com.shoppingmall.orderservice.domain;

public enum OrderStatus {
    READY,START,COMPLETED
}//결제 진입 후에 결제 페이지에서 15분 안에 완료하는지만 확인하면 된다
//결제 중 -> 결제완료 이 부분은 상태확인이 필요없고 결제 API
