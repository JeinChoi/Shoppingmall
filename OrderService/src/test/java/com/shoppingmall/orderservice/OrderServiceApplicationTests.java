package com.shoppingmall.orderservice;

import com.shoppingmall.orderservice.controller.ItemFeignClient;
import com.shoppingmall.orderservice.controller.UserFeignClient;
import com.shoppingmall.orderservice.domain.DeliveryStatus;
import com.shoppingmall.orderservice.domain.Order;
import com.shoppingmall.orderservice.domain.OrderItem;
import com.shoppingmall.orderservice.domain.OrderStatus;
import com.shoppingmall.orderservice.dto.FindItemDto;
import com.shoppingmall.orderservice.dto.FindUserDto;
import com.shoppingmall.orderservice.dto.OrderItemDto;
import com.shoppingmall.orderservice.dto.feignClientDto.ItemFeignResponse;
import com.shoppingmall.orderservice.dto.feignClientDto.UserFeignResponse;
import com.shoppingmall.orderservice.repository.OrderBulkRepository;
import com.shoppingmall.orderservice.repository.OrderItemBulkRepository;
import com.shoppingmall.orderservice.repository.OrderItemRepository;
import com.shoppingmall.orderservice.repository.OrderRepository;
import com.shoppingmall.orderservice.service.OrderService;
import com.shoppingmall.orderservice.service.RedisService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.shoppingmall.orderservice.domain.DeliveryStatus.READY;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
//@ActiveProfiles("test")
//@TestPropertySource(locations = "classpath:application-test.yml")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderServiceApplicationTests {

    @Autowired
    OrderService orderService;
    @Autowired
    RedisService redisService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ItemFeignClient itemFeignClient;
    @Autowired
    UserFeignClient userFeignClient;
    @Autowired
    EntityManager em;

        @Test
        @DisplayName("스레드100개 생성해서 동시성 테스트")
    void createOrder() throws InterruptedException {

        int threadCount = 10000;
        int initialStock = 5000;  // 초기 재고 수
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        // 초기 재고 설정
       // redisService.setValue("101", String.valueOf(initialStock));

        for (int i = 1; i <= 10000; i++) {
            int finalI = (int) i;
            executorService.submit(() -> {
                try {
                    OrderItemDto orderItemDto = new OrderItemDto(Long.parseLong(String.valueOf(finalI)), 101, 1);
                    orderService.order(orderItemDto);

                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println("주문 실패: " + e.getMessage()+ "주문한 유저 :"+ String.valueOf(finalI));
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean completed = latch.await(30, TimeUnit.SECONDS);
        if (!completed) {
            System.out.println("일부 스레드가 시간 내에 완료되지 않았습니다.");
        }

        executorService.shutdown();

        String remainingStock = redisService.getValues(101+"");
        System.out.println("성공한 주문 수: " + successCount.get());
        System.out.println("잔여 재고수 = " + remainingStock);

        assertThat(successCount.get()).isEqualTo(threadCount);
        assertThat(10000-successCount.get()).isEqualTo(initialStock - threadCount);
    }

    @Test
    @DisplayName("bulk insert")
    void 벌크_insert() {
        long startTime = System.currentTimeMillis();

        for (int i = 1; i <=10000; i++) {
            OrderItemDto orderItemDto = new OrderItemDto(Long.parseLong(String.valueOf(i)), 101, 1);

            orderService.order(orderItemDto);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("---------------------------------");
        System.out.printf("수행시간: %d\n", endTime - startTime);
        System.out.println("---------------------------------");
    }

    @Test
    @DisplayName("단순 Order 객체 생성")
    void Order_insert() {
        long startTime = System.currentTimeMillis();

        for (long i = 1; i <=10000; i++) {
            int finalI = (int) i;

            OrderItemDto orderItemDto = new OrderItemDto(Long.parseLong(String.valueOf(finalI)), 101, 1);

            orderService.order(orderItemDto);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("---------------------------------");
        System.out.printf("수행시간: %d\n", endTime - startTime);
        System.out.println("---------------------------------");

    }
}
