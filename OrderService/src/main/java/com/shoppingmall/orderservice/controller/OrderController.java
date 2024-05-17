package com.shoppingmall.orderservice.controller;

import com.shoppingmall.orderservice.dto.*;
import com.shoppingmall.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
        private final OrderService orderService;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);


    @PatchMapping("/refund")
    public ResponseEntity<?> refundItem(@RequestBody RefundOrderDto refundOrderDto){
        boolean possible = orderService.refund(refundOrderDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/order")//READY
    public ResponseEntity<?> orderItem(@RequestBody OrderItemDto orderItemDto){
        //토큰 검사 및 dto에 상품 id, 개수, 주소, 전화번호 필요
        //여기서 일단 재고 -를 해준다
        //timeout 이 됐으면 다시 + 해준다
        //여기서 order 생성하는 걸로.
        //orderId 반환해야 한다
        Long orderId = orderService.order(orderItemDto);
        //orderService.save(order);

        return new ResponseEntity<>(orderItemDto,HttpStatus.OK);
    }

    @PostMapping("/startPaying")//가격이랑 ORDERID READY->START
    public ResponseEntity<?> paying(@RequestBody OrderIdDto orderIdDto){
        orderService.updateOrderStatus(orderIdDto);

        return new ResponseEntity<>(orderIdDto,HttpStatus.OK);
    }

    @PostMapping("/completePaying")//START->COMPLETE
    public ResponseEntity<?> completePaying(@RequestBody OrderIdDto orderIdDto){
        //결제가 성공할 수도 실패할 수도 있다.
        if(orderIdDto.isSuccess()){
            orderService.updateOrderStatus(orderIdDto);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {//결제가 실패하면 order 엔티티가 삭제 그렇기 때문에 여기서는 상태변화 체크 안해줘도 됨
            //여기서도 재고 다시 +
            orderService.deleteOne(orderIdDto);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }
    @PostMapping("/order/wishlist")
    public ResponseEntity<?> orderWishlist(@RequestBody OrderWishListDto orderWishListDto){
        orderService.orderWishList(orderWishListDto);

        return new ResponseEntity<>(orderWishListDto,HttpStatus.OK);
    }

    @GetMapping("/orderlist")
    public List<OrderListDto> orderList(@RequestBody FindOrderListDto findOrderListDto){
        return orderService.findOrderList(findOrderListDto.getUserId());
    }
//어떤 상품을 주문 했는지도 알려줘야 한다 어떤 아이템을 얼마로 주문 했는지
}