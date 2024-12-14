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
import com.shoppingmall.orderservice.repository.*;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.shoppingmall.orderservice.domain.DeliveryStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderBulkRepository orderBulkRepository;

    private final OrderSaveService orderSaveService;

    private final OrderItemRepository orderItemRepository;
    private final OrderItemBulkRepository orderItemBulkRepository;

    private final RedisService redisService;
    private final ItemFeignClient itemFeignClient;
    private final UserFeignClient userFeignClient;

    private final RedissonClient redisson;

    private final List<Order> orders = new ArrayList<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    private final List<OrderItem> orderItems = new LinkedList<>();
    private final int orderItemsBatchSize=100;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final EntityManager em;


//    public void order(OrderItemDto orderItemDto){
//        ItemFeignResponse itemFeignResponse = itemFeignClient.findItemById(new FindItemDto(orderItemDto.getItemId()));
//        UserFeignResponse userFeignResponse = userFeignClient.findUserByLoginId(new FindUserDto(orderItemDto.getUserId()));
//        //주문상품 생성
//        OrderItem orderItem = OrderItem.createOrderItem(
//                orderItemDto.getItemId(),
//                itemFeignResponse.getPrice(),
//                orderItemDto.getCount());
//
//        //주문 생성
//        Order order = Order.createOrder(
//                userFeignResponse.getUserId(), READY, OrderStatus.READY,
//                orderItem,userFeignResponse.getCity(),
//                userFeignResponse.getStreet(),userFeignResponse.getZipcode());
//
//        RLock lock = redisson.getLock("order:"+ orderItemDto.getItemId());
//        boolean isLocked=false;
//        try{
//            isLocked=lock.tryLock(500,300,TimeUnit.MILLISECONDS);
//            if(isLocked){
//                Long presentStock =redisService.getValues(orderItemDto.getItemId());
//                //presentstock가 0인 경우도 분기처리 필요
//
//               // itemFeignClient.updateStock(new UpdateStockDto(orderItem.getItemId(),orderItem.getCount(),false));
//                if(presentStock<orderItemDto.getCount())
//                    throw new IllegalArgumentException("주문 수량이 재고 수량보다 많습니다");
//                else if(presentStock==0)
//                    throw new IllegalArgumentException("재고 수량이 0입니다");
//                long updateStock = presentStock-orderItemDto.getCount();
//                if(updateStock==0) {
//                    //updateState(orderItemDto.getItemId());
//                }
//                redisService.setValues(orderItemDto.getItemId(),updateStock);
//            }else {
//                throw new IllegalStateException("락 획득 실패");
//            }
//        } catch(Exception e){
//            handleOrderFailure(order,orderItem, e);//Redis와 MySQL 모두 롤백
//        }finally{
//            if(lock!=null && lock.isHeldByCurrentThread()){
//                lock.unlock();
//            }
//        }
//        //saveOrder(orderItem,userFeignResponse);
//       addOrder(orderItem,order,userFeignResponse);
//
//      // orderRepository.save(order);
//    }
public void order(OrderItemDto orderItemDto){//itemid,userid,count
    //주문상품 생성
    OrderItem orderItem = OrderItem.createOrderItem(
            orderItemDto.getItemId(),
            orderItemDto.getCount());

    //주문 생성
    Order order = Order.createOrder(orderItemDto.getUserId(),
            DeliveryStatus.READY, OrderStatus.READY, orderItem);

    RLock lock = redisson.getLock("order:"+ orderItemDto.getItemId());
    boolean isLocked=false;
    try{
        isLocked=lock.tryLock(50,30,TimeUnit.SECONDS);
        if(isLocked){
            Long presentStock = Long.parseLong(redisService.getValues(orderItemDto.getItemId()+""));
             if(presentStock<orderItemDto.getCount())
                throw new IllegalArgumentException("주문 수량이 재고 수량보다 많습니다");
            else if(presentStock==0)
                throw new IllegalArgumentException("재고 수량이 0입니다");
            long updateStock = presentStock-orderItemDto.getCount();
            if(updateStock==0) {
                itemFeignClient.updateState(new ItemUpdateDto(orderItemDto.getItemId()));

            }
            redisService.setValues(orderItemDto.getItemId()+"",updateStock+"");
        }else {
            throw new IllegalStateException("락 획득 실패");
        }
    } catch(Exception e){
        em.clear();
        logger.info(e.toString());
    }finally{
        lock.unlock();
    }
    orderSaveService.saveOrdersNOrderItem(order,orderItem);
}


    public void orderBulk(OrderItemDto orderItemDto){//itemid,userid,count
        ItemFeignResponse itemFeignResponse = itemFeignClient.findItemById(new FindItemDto(orderItemDto.getItemId()));
        UserFeignResponse userFeignResponse = userFeignClient.findUserByLoginId(new FindUserDto(orderItemDto.getUserId()));
        //주문상품 생성

        OrderDto orderDto = OrderDto.builder().
                userId(orderItemDto.getUserId()).
                orderStatus(OrderStatus.READY).
                deliveryStatus(DeliveryStatus.READY).
                city(userFeignResponse.getCity()).
                street(userFeignResponse.getStreet()).
                zipcode(userFeignResponse.getZipcode()).build();

        OrderItemBulkDto orderItemBulkDto = OrderItemBulkDto.builder().
                userId(orderItemDto.getUserId()).
                itemId(orderItemDto.getItemId()).
                count(orderItemDto.getCount()).
                price(itemFeignResponse.getPrice()).
                build();

        RLock lock = redisson.getLock("order:"+ orderItemDto.getItemId());
        boolean isLocked=false;
        try{
            isLocked=lock.tryLock(10,30,TimeUnit.SECONDS);
            if(isLocked){
                Long presentStock = Long.parseLong(redisService.getValues(orderItemDto.getItemId()+""));
                //presentstock가 0인 경우도 분기처리 필요

                // itemFeignClient.updateStock(new UpdateStockDto(orderItem.getItemId(),orderItem.getCount(),false));
                if(presentStock<orderItemDto.getCount())
                    throw new IllegalArgumentException("주문 수량이 재고 수량보다 많습니다");
                else if(presentStock==0)
                    throw new IllegalArgumentException("재고 수량이 0입니다");
                long updateStock = presentStock-orderItemDto.getCount();
                if(updateStock==0) {
                    //updateState(orderItemDto.getItemId());
                }
                redisService.setValues(orderItemDto.getItemId()+"",updateStock+"");
                logger.info("redis value 재고량 ::::: ",updateStock);
            }else {
                throw new IllegalStateException("락 획득 실패");
            }
        } catch(Exception e){
        //   handleOrderFailure(order,orderItem, e);//Redis와 MySQL 모두 롤백
        }finally{
            lock.unlock();
        }
         addOrderDto(orderDto,orderItemBulkDto,userFeignResponse);
    }



    private static List<OrderDto> orderDtos = new LinkedList<>();
    private static List<OrderItemBulkDto> orderItemBulkDtos = new LinkedList<>();
    public void addOrderDto(OrderDto orderDto, OrderItemBulkDto orderItemBulkDto, UserFeignResponse userFeignResponse){

        boolean saveOrders = orders.size()%batchSize==0;
        boolean saveOrderItems = orderItems.size()%batchSize==0;
        //order.setOrderItem(orderItem);
        orderDtos.add(orderDto);
        orderItemBulkDtos.add(orderItemBulkDto);

        if(orders.size()>=batchSize){

           orderItemBulkRepository.saveAll(orderDtos,orderItemBulkDtos);
            // orderItemRepository.saveAll(orderItems);

            orderDtos.clear();
            orderItemBulkDtos.clear();
        }
    }
    void handleOrderFailure(Order order, OrderItem orderItem,Exception e){
        logger.info("롤백 처리 됐는지", e);

    }


    @Scheduled(fixedDelay = 60000) // 1분마다 실행
    @Transactional
    public void scheduledSave() {
        orderRepository.saveAll(orders);
        orders.clear();
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
        cal.add(Calendar.DATE,1);//배송 완료된 날에서 하루를 더한다
        returnDate= new Timestamp(cal.getTime().getTime());
        logger.info("환불 가능 날짜 {}",returnDate);

        if(findOrder.getDeliveryStatus().equals(COMPLETED)//배송 상태가 completed 되어 있고
                && findOrder.getModifiedAt().before(returnDate)){//completed되고 나서 하루 미만 이어야 함
            findOrder.updateOrderStatusToRefund();//refund로 상태 변경
            return true;
        }
        else{//환불 가능 날짜 이후라면 false 반환
            return false;
        }

}
    public void orderWishList(OrderWishListDto orderWishListDto){
        UserFeignResponse userFeignResponse = userFeignClient.findUserByLoginId(new FindUserDto(orderWishListDto.getUserId()));
        List<WishItemListDto> wishList = itemFeignClient.wishlist(new WishListDto(orderWishListDto.getUserId()));
        List<Order> orderList = new ArrayList<>();
        List<OrderItem> orderItemList = new ArrayList<>();
        for(WishItemListDto one : wishList){

            OrderItem orderItem = OrderItem.createOrderItem(one.getItemId(),one.getCount());
            orderItemList.add(orderItem);

            orderList.add(new Order(
                    orderWishListDto.getUserId(),
                    READY,OrderStatus.START,
                    orderItem));

        }
        orderItemRepository.saveAll(orderItemList);
        orderRepository.saveAll(orderList);
        itemFeignClient.deleteAllWishList(new WishListDto(orderWishListDto.getUserId()));
    }

    //orderitemlist도 가져와야한다.
    public List<OrderListDto> findOrderList(long userId){
        List<OrderListDto> orderListdto = new ArrayList<>();
        List<Order> orderlist = orderRepository.findAllByUserId(userId);

//        for(Order one : orderlist){
//            ItemFeignResponse itemFeignResponse = itemFeignClient.findItemById(new FindItemDto(one.getOrderItem().getItemId()));
//
//            orderListdto.add(new OrderListDto(
//                    one.getOrderId(),
//                    one.getOrderItem().getOrderItemId(),
//                    //itemId
//                    itemFeignResponse.getItemId(),
//                    one.getDeliveryStatus(),
//                    one.getOrderDate(),
//                    one.getOrderItem().getPrice(),
//                    one.getOrderItem().getCount()*one.getOrderItem().getPrice(),
//                    //itemName
//                    itemFeignResponse.getItemName()
//            ));
//        }
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

    @Scheduled(cron="0 39 15 * * *")//order 중에 refund 상태이면서 modified 날짜가 하루 이하로 차이나는 것만 가져오기
    public void updateRefundCompletedStock(){
        List<Order> orderList = orderRepository.findAllRefund(OrderStatus.REFUND);
        if(orderList.isEmpty())
            return;

        for(Order order : orderList){
            OrderItem orderItem = order.getOrderItem();
            itemFeignClient.updateStock(new UpdateStockDto(orderItem.getItemId(),
                    orderItem.getCount(),true));
            order.updateOrderStatusToRefundCompleted();
        }
    }
    @Scheduled(cron="0 0/5 * * * ?")
    public void checkOrderStatus(){//order의 orderstatus가 여전히 0이라면 삭제 및 재고 + 처리
        List<Order> orderList = orderRepository.findAllTimeout();
        List<OrderItem> orderItemList = new ArrayList<>();
        //where 조건절에 시간을 비교하는 sql문을 생성
        if(orderList.isEmpty())
            return;

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
