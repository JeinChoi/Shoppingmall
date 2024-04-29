package com.shoppingmall.preorder.service;


import com.shoppingmall.preorder.domain.*;
import com.shoppingmall.preorder.dto.ChangeWishItemDto;
import com.shoppingmall.preorder.dto.OrderDto;
import com.shoppingmall.preorder.dto.WishListDto;
import com.shoppingmall.preorder.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class WishItemService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final WishItemRepository wishItemRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final OrderService orderService;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);

    public void save(WishItem wishItem) {
        wishItemRepository.save(wishItem);
    }


    public List<WishItem> findWishItemList(long userId){
        User findUser = userRepository.findOne(userId);
        return wishItemRepository.findAllByUser(findUser);

    }

    public ResponseEntity<?> updateWishItem(ChangeWishItemDto changeWishItemDto){
        WishItem findWishItem = wishItemRepository.findById(changeWishItemDto.getWishItemId());
        findWishItem.updateWishItem(changeWishItemDto.getCount());
        return new ResponseEntity<>(findWishItem, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteWishItem(long wishItemId){
       wishItemRepository.deleteById(wishItemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public List<WishItem> orderWholeWishList(long userId){
        User findUser = userRepository.findOne(userId);
        List<WishItem> list = wishItemRepository.findAllByUser(findUser);
        List<OrderItem> orderItemList = new ArrayList<>();

        //여기서 변환해줘야함
        for(WishItem one : list){
            orderItemList.add(OrderItem.createOrderItem(one.getWishitem_item(),one.getOrderPrice(),one.getCount()));
        }
        Order order = new Order(findUser,"ON_DELIVERY",findUser.getCity(), findUser.getStreet(), findUser.getZipcode(),orderItemList);

        orderService.order(findUser,orderItemList,order);
        wishItemRepository.deleteAll(list);
        return list;
    }

}