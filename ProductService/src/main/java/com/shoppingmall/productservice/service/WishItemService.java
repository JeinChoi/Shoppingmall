package com.shoppingmall.productservice.service;


import com.shoppingmall.productservice.controller.OrderFeignClient;
import com.shoppingmall.productservice.controller.UserFeignClient;
import com.shoppingmall.productservice.domain.WishItem;
import com.shoppingmall.productservice.dto.ChangeWishItemDto;
import com.shoppingmall.productservice.dto.OrderItemDto;
import com.shoppingmall.productservice.dto.OrderWishListDto;
import com.shoppingmall.productservice.repository.ItemRepository;
import com.shoppingmall.productservice.repository.WishItemRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class WishItemService {

    private final ItemRepository itemRepository;
    private final WishItemRepository wishItemRepository;
    private final OrderFeignClient orderFeignClient;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WishItemService.class);

    public void save(WishItem wishItem) {
        wishItemRepository.save(wishItem);
    }

public void update(ChangeWishItemDto changeWishItemDto){
        Optional<WishItem> findWishItem = wishItemRepository.findById(changeWishItemDto.getWishItemId());
        findWishItem.get().updateWishItem(changeWishItemDto.getCount());
}
    public List<WishItem> findWishItemList(long userId){
        return wishItemRepository.findAllByUserId(userId);

    }

    public ResponseEntity<?> deleteWishItem(long wishItemId){
       wishItemRepository.deleteById(wishItemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    public void deleteAllWishItem(long userId){
        wishItemRepository.deleteAllByUserId(userId);
    }
    public List<WishItem> orderWholeWishList(long userId){

        List<WishItem> list = wishItemRepository.findAllByUserId(userId);
        List<OrderItemDto> orderItemList = new ArrayList<>();
//여기서 dto로 list 만들고 itemId, count,price
        //list랑 userid 같이 보내기

        for(WishItem one : list){
            orderItemList.add(new OrderItemDto(one.getUserId(),one.getItem().getItemId(),one.getCount(),one.getOrderPrice()));
        }

        orderFeignClient.orderWishlist(new OrderWishListDto(orderItemList,userId));

        //본래 장바구니에 있던 항목들은 삭제 처리
        wishItemRepository.deleteAll(list);

        return list;
    }

//    public List<OrderItem> orderSelectWishList(SelectWishListDto selectWishListDto){
//        List<OrderItem> orderItemList = new ArrayList<>();
//        User findUser = userRepository.findOne(selectWishListDto.getUserId());
//        for(Long one : selectWishListDto.getSelectlist()){
//            WishItem temp = wishItemRepository.findById(one).get();
//            orderItemList.add(OrderItem.createOrderItem(temp.getWishitem_item(),temp.getOrderPrice(),temp.getCount()));
//            wishItemRepository.deleteById(one);
//        }
//        Order order = new Order(findUser,"READY",findUser.getCity(), findUser.getStreet(), findUser.getZipcode(),orderItemList);
//
//        orderService.order(findUser,orderItemList,order);
//
//        return orderItemList;
//    }
}