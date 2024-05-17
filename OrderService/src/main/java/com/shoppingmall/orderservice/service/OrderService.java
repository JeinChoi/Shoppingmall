package com.shoppingmall.orderservice.service;

import com.shoppingmall.orderservice.config.RedissonLock;
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
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import java.util.concurrent.TimeUnit;

import static com.shoppingmall.orderservice.domain.DeliveryStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final RedisService redisService;
    private final ItemFeignClient itemFeignClient;
    private final UserFeignClient userFeignClient;

    private final RedissonClient redissonClient;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(OrderService.class);

    public void save(Order order) {
        orderRepository.save(order);
    }


    public void save(OrderItem orderItem){
        orderItemRepository.save(orderItem);
    }


    public long order(OrderItemDto orderItemDto){
        ItemFeignResponse itemFeignResponse = itemFeignClient.findItemById(new FindItemDto(orderItemDto.getItemId()));
        UserFeignResponse userFeignResponse = userFeignClient.findUserByLoginId(new FindUserDto(orderItemDto.getUserId()));
        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(orderItemDto.getItemId(), itemFeignResponse.getPrice(), orderItemDto.getCount());

        int presentStock = Integer.parseInt(redisService.getValues(orderItemDto.getItemId()+""));
        //presentstock가 0인 경우도 분기처리 필요

        if(presentStock<orderItemDto.getCount())
            throw new IllegalArgumentException("주문 수량이 재고 수량보다 많습니다");
        else if(presentStock==0)
            throw new IllegalArgumentException("재고 수량이 0입니다");

//        int updateStock = presentStock-orderItemDto.getCount();
//        if(updateStock==0) {
//            itemFeignClient.updateState(new ItemUpdateDto(orderItemDto.getItemId()));
//        }

       // itemFeignClient.updateStock(new UpdateStockDto(orderItem.getItemId(),orderItem.getCount(),false));
//        manageRedis(orderItem,userFeignResponse);
//        orderItemRepository.save(orderItem);
//        Order order = new Order(
//                orderItemDto.getUserId(), READY, OrderStatus.READY,
//                orderItem,userFeignResponse.getCity(),
//                userFeignResponse.getStreet(),userFeignResponse.getZipcode());
//
//        orderRepository.save(order);
       // redisService.setValues(orderItemDto.getItemId()+"",updateStock+"");
        //이부분 product feign client로 바꾸기 캐싱 처리하는 부분. 여기서 안할거임
//        manageRedis(orderItem,userFeignResponse);
//        orderItemRepository.save(orderItem);
//        Order order = new Order(
//                userFeignResponse.getUserId(), READY, OrderStatus.READY,
//                orderItem,userFeignResponse.getCity(),
//                userFeignResponse.getStreet(),userFeignResponse.getZipcode());
//
//        orderRepository.save(order);
        saveOrder(orderItem,userFeignResponse);
        manageRedis(orderItem);
        return 1;
        //return order.getOrderId();
    }
    public void redisTest(){

        int presentStock = Integer.parseInt(redisService.getValues(126+""));
        //presentstock가 0인 경우도 분기처리 필요

        if(presentStock<1)
            throw new IllegalArgumentException("주문 수량이 재고 수량보다 많습니다");

        int updateStock = presentStock-1;
//        if(updateStock==0)
//            itemFeignClient.updateState(new ItemUpdateDto(126));
        String lockName = "ITEM" + 126;
        RLock rLock = redissonClient.getLock(lockName);

        long waitTime = 5L;
        long leaseTime = 3L;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        try {
            boolean available = rLock.tryLock(waitTime, leaseTime, timeUnit);
            if(!available){
                logger.info("lock 획득 실패={}",lockName);
                throw new RuntimeException();
            }
            redisService.setValues(126+"",updateStock+""); //만약 여기서 false가 반환되면 item 상태는 품절로 바뀌고 주문 실패 띄우기
            //애초에 품절 상태면 상세보기에서 구매도 안되도록 처리해야한다
            //만약 주문 수량이 재고량 보다 많으면

        }catch (InterruptedException e){
            //락을 얻으려고 시도하다가 인터럽트를 받았을 때 발생하는 예외
            logger.info("lock 얻으려고 시도하다가 실패 ");
            throw new RuntimeException();
        }finally{
            try{
                rLock.unlock();
                logger.info("unlock complete: {}", rLock.getName());
            }catch (IllegalMonitorStateException e){
                //이미 종료된 락일 때 발생하는 예외
                throw new RuntimeException();
            }
        }
       // redisService.setValues(126+"",updateStock+"");
    }

    public void saveOrder(OrderItem orderItem, UserFeignResponse userFeignResponse){
        orderItemRepository.save(orderItem);
        Order order = new Order(
                userFeignResponse.getUserId(), READY, OrderStatus.READY,
                orderItem,userFeignResponse.getCity(),
                userFeignResponse.getStreet(),userFeignResponse.getZipcode());

        orderRepository.save(order);
    }
    @RedissonLock(value="#itemId")
    public void manageRedis(OrderItem orderItem){

        int presentStock = Integer.parseInt(redisService.getValues(orderItem.getItemId()+""));
        int updateStock = presentStock-orderItem.getCount();
        //presentstock가 0인 경우도 분기처리 필요
        logger.info("현재 재고 량 :::: {}",presentStock);
        try{
            if(presentStock<1)
                throw new RuntimeException("주문 수량이 재고 수량보다 많습니다");
            else if(updateStock<1)
                throw new RuntimeException("주문 수량이 재고 수량보다 많습니다");
        }catch(Exception e){
            System.err.println("오류: "+e.getMessage());
        }

            redisService.setValues(orderItem.getItemId()+"",updateStock+""); //만약 여기서 false가 반환되면 item 상태는 품절로 바뀌고 주문 실패 띄우기
            //애초에 품절 상태면 상세보기에서 구매도 안되도록 처리해야한다
            //만약 주문 수량이 재고량 보다 많으면
       // itemFeignClient.updateStock(new UpdateStockDto(126,1,false));
        // redisService.setValues(126+"",updateStock+"");

    }
    public void updateOrderStatus(OrderIdDto orderIdDto){
        Order findOrder = orderRepository.findById(orderIdDto.getOrderId()).get();
        findOrder.updateOrderStatus();

    }
    public void deleteOne(OrderIdDto orderIdDto){

        orderRepository.deleteById(orderIdDto.getOrderId());
    }
public boolean refund(RefundOrderDto refundOrderDto){
        Order findOrder = orderRepository.findById(refundOrderDto.getOrderId()).get();

        Date date = new Date();
        date.setTime(findOrder.getModifiedAt().getTime());

        Timestamp returnDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE,1);
        returnDate= new Timestamp(cal.getTime().getTime());
        logger.info("환불 날짜 {}",returnDate);
        if(findOrder.getDeliveryStatus().equals(COMPLETED)
                && findOrder.getModifiedAt().before(returnDate)){//&&완료 이후 날짜가 하루 미만 이어야 함..)
            findOrder.updateOrderStatusToRefund();
            return true;
        }
        else{//환불 가능 날짜 이후라면 false 반환
            //findOrder.updateOrderStatusToRefundImpossible();
            return false;
        }

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
            switch (order.getDeliveryStatus()){
                case READY:
                    order.updateDeliveryStatus(ON_DELIVERY);
                    break;
                case ON_DELIVERY:
                    order.updateDeliveryStatus(COMPLETED);
                    break;
            }
        }
    }

    @Scheduled(cron="0 39 15 * * *")//order 중에 refund 상태이면서 modified 날짜가 하루가 차이나는 것만 가져오기
    public void updateRefundCompletedStock(){
        List<Order> orderList = orderRepository.findAllRefund(OrderStatus.REFUND);

        for(Order order : orderList){
            itemFeignClient.updateStock(new UpdateStockDto(order.getOrderItem().getItemId(),
                    order.getOrderItem().getCount(),true));
            order.updateOrderStatusToRefundCompleted();
        }

    }
    @Scheduled(cron="0 0/5 * * * ?")
    public void checkOrderStatus(){//order의 orderstatus가 여전히 0이라면 삭제 및 재고 + 처리
        List<Order> orderList = orderRepository.findAllTimeout();
        List<OrderItem> orderItemList = new ArrayList<>();
        //where 조건절에 시간을 비교하는 sql문을 생성
        for(Order order : orderList){
            itemFeignClient.updateStock(new UpdateStockDto(order.getOrderItem().getItemId(),order.getOrderItem().getCount(),true));
            orderItemList.add(order.getOrderItem());
        }
        //가져온 orderlist의 주문 수량
        //삭제 처리 전. 재고 수정 해줘야함
        orderRepository.deleteAll(orderList);
        orderItemRepository.deleteAll(orderItemList);
    }
}
