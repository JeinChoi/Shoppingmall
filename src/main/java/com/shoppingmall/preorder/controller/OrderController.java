package com.shoppingmall.preorder.controller;

import com.shoppingmall.preorder.domain.Item;
import com.shoppingmall.preorder.domain.Order;
import com.shoppingmall.preorder.domain.OrderItem;
import com.shoppingmall.preorder.jwt.JwtFilter;
import com.shoppingmall.preorder.jwt.TokenProvider;
import com.shoppingmall.preorder.domain.User;
import com.shoppingmall.preorder.dto.*;
import com.shoppingmall.preorder.repository.ItemListRepository;
import com.shoppingmall.preorder.repository.ItemRepository;
import com.shoppingmall.preorder.repository.OrderItemRepository;
import com.shoppingmall.preorder.repository.OrderRepository;
import com.shoppingmall.preorder.service.ItemService;
import com.shoppingmall.preorder.service.OrderItemService;
import com.shoppingmall.preorder.service.OrderService;
import com.shoppingmall.preorder.service.UserService;
import com.shoppingmall.preorder.shopinfo.ShopInfoSearch;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;
    private final ShopInfoSearch shopInfoSearch;
    private final ItemListRepository itemListRepository;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final ItemService itemService;
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    @GetMapping("/")//db 저장용
    public ResponseEntity<?> saveItems(OrderDto orderDto) {

        String result = shopInfoSearch.search();

        List<Item> list = shopInfoSearch.fromJSONtoItems(result);
        itemListRepository.saveAll(list);
        return new ResponseEntity<>(HttpStatus.OK);

    }
    @PostMapping("/orderItem")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> orderItem(@RequestBody OrderItemDto orderItemDto){
        //토큰 검사 및 dto에 상품 id, 개수, 주소, 전화번호 필요
        User findUser = userService.findUser(orderItemDto.getUserId());
        Item findItem = itemService.findOne(orderItemDto.getItemId());
        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(findItem, findItem.getPrice(), orderItemDto.getCount());

        orderItemService.save(orderItem);

        return new ResponseEntity<>(orderItem.getOrderItemId(),HttpStatus.OK);
    }
    @PostMapping("/order")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> order(@RequestBody OrderDto orderDto){
        //토큰 검사 및 dto에 상품 id, 개수, 주소, 전화번호 필요
        orderService.orderDto(orderDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    //상품 리스트와 상세 조회 기능은 회원가입이 필요없이 가능하다
    @GetMapping("/list")
    public ResponseEntity<?> list(){
        List<Item> list = itemService.findAll();

        List<ItemListDto> resultList = list.stream().map(i ->{
            try{
                return new ItemListDto(i.getItemName(),i.getPrice(),i.getStockQuantity());
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;

        }).toList();
        return new ResponseEntity<>(resultList,HttpStatus.OK);
    }
    @GetMapping("/detail/{itemId}")
    public ResponseEntity<?> productDetail(@PathVariable("itemId") Long itemId){
        Item item = itemService.findOne(itemId);
        ItemDetailDto itemDetailDto = new ItemDetailDto(
                item.getItemName(),
                item.getPrice(),
                item.getStockQuantity(),
                item.getItemStateName(),
                item.getDetail()
        );
        return new ResponseEntity<>(itemDetailDto, HttpStatus.OK);

    }

}