package com.shoppingmall.orderservice.service;

import com.shoppingmall.orderservice.controller.ItemFeignClient;
import com.shoppingmall.orderservice.controller.UserFeignClient;
import com.shoppingmall.orderservice.domain.DeliveryStatus;
import com.shoppingmall.orderservice.domain.Order;
import com.shoppingmall.orderservice.domain.OrderItem;

import com.shoppingmall.orderservice.domain.OrderStatus;
import com.shoppingmall.orderservice.dto.*;

import com.shoppingmall.orderservice.dto.feignClientDto.ItemFeignResponse;
import com.shoppingmall.orderservice.dto.feignClientDto.UserFeignResponse;

import com.shoppingmall.orderservice.dto.feignClientDto.WishItemListDto;
import com.shoppingmall.orderservice.repository.OrderItemRepository;
import com.shoppingmall.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import java.sql.Timestamp;
import static com.shoppingmall.orderservice.domain.DeliveryStatus.READY;
import static com.shoppingmall.orderservice.domain.OrderStatus.START;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final RedisService redisService;
    private final ItemFeignClient itemFeignClient;
    private final UserFeignClient userFeignClient;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(OrderService.class);

    public void save(Order order) {
        orderRepository.save(order);
    }


    public void save(OrderItem orderItem){
        orderItemRepository.save(orderItem);
    }


    public Long order(OrderItemDto orderItemDto){
        ItemFeignResponse itemFeignResponse = itemFeignClient.findItemById(new FindItemDto(orderItemDto.getItemId()));
        UserFeignResponse userFeignResponse = userFeignClient.findUserByLoginId(new FindUserDto(orderItemDto.getUserId()));
        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(orderItemDto.getItemId(), itemFeignResponse.getPrice(), orderItemDto.getCount());

        int presentStock = Integer.parseInt(redisService.getValues(orderItemDto.getItemId()+""));
//presentstock가 0인 경우도 분기처리 필요

        if(presentStock<orderItemDto.getCount())
            throw new IllegalArgumentException("주문 수량이 재고 수량보다 많습니다");

        int updateStock = presentStock-orderItemDto.getCount();
        if(updateStock==0)
            itemFeignClient.updateState(new ItemUpdateDto(orderItemDto.getItemId()));
        redisService.setValues(orderItemDto.getItemId()+"",updateStock+""); //만약 여기서 false가 반환되면 item 상태는 품절로 바뀌고 주문 실패 띄우기
        //애초에 품절 상태면 상세보기에서 구매도 안되도록 처리해야한다
        //만약 주문 수량이 재고량 보다 많으면

        orderItemRepository.save(orderItem);
        Order order = new Order(
                orderItemDto.getUserId(), READY, OrderStatus.READY,
                orderItem,userFeignResponse.getCity(),
                userFeignResponse.getStreet(),userFeignResponse.getZipcode());

        orderRepository.save(order);

        return order.getOrderId();
    }
    public void updateOrderStatus(OrderIdDto orderIdDto){
        Order findOrder = orderRepository.findById(orderIdDto.getOrderId()).get();
        findOrder.updateOrderStatus();

    }
    public void deleteOne(OrderIdDto orderIdDto){

        orderRepository.deleteById(orderIdDto.getOrderId());
    }

    public void orderWishList(OrderWishListDto orderWishListDto){
        UserFeignResponse userFeignResponse = userFeignClient.findUserByLoginId(new FindUserDto(orderWishListDto.getUserId()));
        List<WishItemListDto> wishList = itemFeignClient.wishlist(new WishListDto(orderWishListDto.getUserId()));
        List<Order> orderList = new ArrayList<>();
        List<OrderItem> orderItemList = new ArrayList<>();
        for(WishItemListDto one : wishList){

            OrderItem orderItem = OrderItem.createOrderItem(one.getItemId(),one.getPrice(),one.getCount());
            orderItemList.add(orderItem);

            orderList.add(new Order(
                    orderWishListDto.getUserId(),
                    READY,OrderStatus.START,
                    orderItem,userFeignResponse.getCity(),
                    userFeignResponse.getStreet(),userFeignResponse.getZipcode()));

        }
        orderItemRepository.saveAll(orderItemList);
        orderRepository.saveAll(orderList);
        itemFeignClient.deleteAllWishList(new WishListDto(orderWishListDto.getUserId()));
    }

    //orderitemlist도 가져와야한다.
    public List<OrderListDto> findOrderList(long userId){
        List<OrderListDto> orderListdto = new ArrayList<>();
        List<Order> orderlist = orderRepository.findAllByUserId(userId);

        for(Order one : orderlist){
            ItemFeignResponse itemFeignResponse = itemFeignClient.findItemById(new FindItemDto(one.getOrderItem().getItemId()));

            orderListdto.add(new OrderListDto(
                    one.getOrderId(),
                    one.getOrderItem().getOrderItemId(),
                    //itemId
                    itemFeignResponse.getItemId(),
                    one.getDeliveryStatus(),
                    one.getCity(),
                    one.getStreet(),
                    one.getZipcode(),
                    one.getOrderDate(),
                    one.getOrderItem().getCount(),
                    one.getOrderItem().getPrice(),
                    one.getOrderItem().getCount()*one.getOrderItem().getPrice(),
                    //itemName
                    itemFeignResponse.getItemName()
            ));
        }
        return orderListdto;
    }


    @Scheduled(cron="0 24 15 * * *")//특정 시각에 배달 상태 변경
    public void updateDeliveryStatus(){
        List<Order> orderList = orderRepository.findAll();

        for(Order order : orderList){
            if(order.getDeliveryStatus().equals(DeliveryStatus.READY))
                order.updateDeliveryStatus(DeliveryStatus.ON_DELIVERY);
            if(order.getDeliveryStatus().equals(DeliveryStatus.ON_DELIVERY))
                order.updateDeliveryStatus(DeliveryStatus.COMPLETED);
        }
    }

    @Scheduled(cron="0 0/5 * * * ?")
    public void checkOrderStatus(){//order의 orderstatus가 여전히 0이라면 삭제 및 재고 + 처리
        List<Order> orderList = orderRepository.findAllTimeout();
        //where 조건절에 시간을 비교하는 sql문을 생성

        //가져온 orderlist의 주문 수량

        orderRepository.deleteAll(orderList);
    }
}
