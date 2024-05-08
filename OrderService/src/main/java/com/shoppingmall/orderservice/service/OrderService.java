package com.shoppingmall.orderservice.service;

import com.shoppingmall.orderservice.controller.ItemFeignClient;
import com.shoppingmall.orderservice.controller.UserFeignClient;
import com.shoppingmall.orderservice.domain.DeliveryStatus;
import com.shoppingmall.orderservice.domain.Order;
import com.shoppingmall.orderservice.domain.OrderItem;

import com.shoppingmall.orderservice.dto.*;

import com.shoppingmall.orderservice.dto.feignClientDto.ItemFeignResponse;
import com.shoppingmall.orderservice.dto.feignClientDto.UserFeignResponse;

import com.shoppingmall.orderservice.dto.feignClientDto.WishItemListDto;
import com.shoppingmall.orderservice.repository.OrderItemRepository;
import com.shoppingmall.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final ItemFeignClient itemFeignClient;
    private final UserFeignClient userFeignClient;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(OrderService.class);
    @Transactional
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Transactional
    public void save(OrderItem orderItem){
        orderItemRepository.save(orderItem);
    }

    @Transactional
    public void order(OrderItemDto orderItemDto){
        ItemFeignResponse itemFeignResponse = itemFeignClient.findItemById(new FindItemDto(orderItemDto.getItemId()));
        UserFeignResponse userFeignResponse = userFeignClient.findUserByLoginId(new FindUserDto(orderItemDto.getUserId()));
        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(orderItemDto.getItemId(), itemFeignResponse.getPrice(), orderItemDto.getCount());
        orderItemRepository.save(orderItem);
        Order order = new Order(
                orderItemDto.getUserId(), DeliveryStatus.READY,
                orderItem,userFeignResponse.getCity(),
                userFeignResponse.getStreet(),userFeignResponse.getZipcode());

        orderRepository.save(order);
    }

    @Transactional
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
                    DeliveryStatus.READY,
                    orderItem,userFeignResponse.getCity(),
                    userFeignResponse.getStreet(),userFeignResponse.getZipcode()));

        }
        orderItemRepository.saveAll(orderItemList);
        orderRepository.saveAll(orderList);
        itemFeignClient.deleteAllWishList(new WishListDto(orderWishListDto.getUserId()));
    }

    @Transactional //orderitemlist도 가져와야한다.
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
}
