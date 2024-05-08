package com.shoppingmall.productservice.controller;


import com.shoppingmall.productservice.domain.Item;
import com.shoppingmall.productservice.domain.WishItem;
import com.shoppingmall.productservice.dto.*;
import com.shoppingmall.productservice.dto.feignClientDto.UserFeignResponse;
import com.shoppingmall.productservice.service.ItemService;
import com.shoppingmall.productservice.service.WishItemService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class WishController {
    private final UserFeignClient userFeignClient;
    private final WishItemService wishItemService;
    private final ItemService itemService;
    private static final Logger logger = LoggerFactory.getLogger(WishController.class);

    @PostMapping("/wish/add/wishItem")
    public ResponseEntity<?> wishItem(@RequestBody WishItemDto wishItemDto){
        //토큰 검사 및 dto에 상품 id, 개수, 주소, 전화번호 필요

//        User findUser = userFeignClient.findUserIdByLoginId(new FindUserDto(wishItemDto.getUserId()));
        //userdto에서 가져와야 할 정보는 ???  userid뿐이다. 왜냐면 걍 아이템 추가하는거니까
        UserFeignResponse findUser = userFeignClient.findUserByLoginId(new FindUserDto(wishItemDto.getUserId()));
        Optional<Item> result = itemService.findOne(wishItemDto.getItemId());
        //주문상품 생성
        Item findItem = result.get();
        WishItem wishItem = WishItem.createWishItem(findUser.getUserId(),findItem, findItem.getPrice(), wishItemDto.getCount());

        wishItemService.save(wishItem);

        return new ResponseEntity<>(wishItem.getWishItemId(),HttpStatus.OK);
    }

    //장바구니 목록 중에 하나의 항목을 수정한다
    @PatchMapping("/wish/change/wishItem")
    public ResponseEntity<?> changeWishItem(@RequestBody ChangeWishItemDto changeWishItemDto){

    //    User findUser = userService.getMyUserWithAuthorities().get();
        wishItemService.update(changeWishItemDto);
        return new ResponseEntity<>(changeWishItemDto.getWishItemId(),HttpStatus.OK);
    }
    
    //장바구니 목록 중에 하나의 항목을 삭제한다
    @DeleteMapping("/wish/delete/wishItem/{wishItemId}")
    public ResponseEntity<?> deleteWishItem(@PathVariable("wishItemId") long wishItemId){
        return wishItemService.deleteWishItem(wishItemId);
    }

    @PostMapping("/wish/list")
    public List<WishItemListDto> wishlist(@RequestBody WishListDto wishListDto){
        List<WishItem> list = wishItemService.findWishItemList(wishListDto.getUserId());

        List<WishItemListDto> resultList = list.stream().map(i ->{
            try{
                return new WishItemListDto(
                        i.getItem().getItemId(),
                        i.getItem().getItemName(),
                        i.getOrderPrice(),
                        i.getCount());
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;

        }).toList();
        return resultList;
    }


    //장바구니에 있는 아이템 전체 주문
    //장바구니에 있는 아이템 삭제
//    @PostMapping("/wish/order/wholeWishlist")
//    public List<WishItem> orderWholeWishList(@RequestBody Map<String,Long> data){
//        return wishItemService.orderWholeWishList(data.get("userId"));
//    }
    @DeleteMapping("/wish/deleteList")
    void deleteAllWishList(@RequestBody WishListDto wishListDto){
        wishItemService.deleteAllWishItem(wishListDto.getUserId());
    }
    //장바구니에 선택한 아이템만 주문
    //장바구니에 있는 선택된 아이템만 삭제
//    @PostMapping("/order/selectWishlist")
//    public List<OrderItem> orderSelectWishList(@RequestBody SelectWishListDto selectWishListDto){
//        return wishItemService.orderSelectWishList(selectWishListDto);
//    }
}
