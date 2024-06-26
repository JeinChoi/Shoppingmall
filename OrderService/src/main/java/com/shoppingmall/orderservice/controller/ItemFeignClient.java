package com.shoppingmall.orderservice.controller;


import com.shoppingmall.orderservice.dto.*;
import com.shoppingmall.orderservice.dto.feignClientDto.ItemFeignResponse;
import com.shoppingmall.orderservice.dto.feignClientDto.WishItemListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="item-service", url="http://localhost:8082")
public interface ItemFeignClient {
    @PostMapping(path="/item/bringItem")
    ItemFeignResponse findItemById(@RequestBody FindItemDto findItemDto);

    @PostMapping(path="/wish/list")
    List<WishItemListDto> wishlist(@RequestBody WishListDto wishListDto);

    @DeleteMapping(path="/wish/deleteList")
    void deleteAllWishList (@RequestBody WishListDto wishListDto);

    @PostMapping(path="/item/updateState")
    void updateState(@RequestBody ItemUpdateDto itemUpdateDto);

    @PostMapping(path="/item/manage")
    void updateStock(@RequestBody UpdateStockDto updateStockDto);

}
