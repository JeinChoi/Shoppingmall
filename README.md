
### 📢 프로젝트 소개


- 고객이 상품을 구매하고, 구매함에 따라 재고, 배송 관리를 해주는 서비스입니다.

- MSA 서비스이기 때문에 개별 배포가 가능하고 장애 격리가 수월합니다.

</br>

### 📢 개발 환경

![java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![spring boot](https://img.shields.io/badge/-Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![spring cloud](https://img.shields.io/badge/-Spring%20Cloud-6DB33F?style=for-the-badge&logo=icloud&logoColor=white)
![spring security](https://img.shields.io/badge/-Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![My SQL](https://img.shields.io/badge/-My%20SQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/-redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Redis](https://img.shields.io/badge/-docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

</br>

- Version: Java 21

- Framework: SpringBoot 3.2.2

- Database: MySQL 8.0

- ORM: JPA/Hibernate
</br>

### 📢 주요 기능
---

- API Gateway를 통해 MSA로 구현. 서비스 기능별로 분리
  
- Feign Client를 통해 내부의 서비스 호출하도록 구현
  
- JWT과 Spring Security를 통해서 인증/인가 구현
  
- 회원가입시 네이버 SMTP를 통한 이메일 인증 구현
  
- Redis에 Redisson 분산락 활용
  
- 주문 상태 관리
  - 주문 후 1일: 배송 중
  - 주문 후 2일: 배송 완료

- 환불 상태 관리
  - 배송 이전에 환불 가능    
  - 환불 후 1일: 환불한 상품의 재고 반영
  </br>  

### 📢 ERD
---
![ERD](https://github.com/JeinChoi/Shoppingmall/assets/59508337/b424afbb-e6ee-4f59-a1ba-9221f38cf5c3)

</br>

### 📢 트러블 슈팅
---
<details>
<summary> 장바구니 전체 주문 기능 구현 시 발생 </summary>
<div markdown="1">
  
문제</br>
- 장바구니 내에 있는 상품 전체 주문 후에 장바구니를 비우려고 할 때 오류 발생
- 
    ```
    Expecting a SELECT Query [org.hibernate.query.sqm.tree.select.SqmSelectStatement], but found org.hibernate.query.sqm.tree.delete.SqmDeleteStatement

    ```

원인</br>    
- JPA는 변경감지로 UPDATE,DELETE 문 생성을 하지만 한번의 변경에 하나의 쿼리만 생성한다. 당시 삭제하려는 튜플이 여러개인 경우가 있었기 때문에 적용이 안됐던 것이었다.
- @Query 어노테이션은 SELECT문만 지원한다. @Query 어노테이션을 강화하여 INSERT,UPDATE,DELETE 쿼리에도 사용 가능하도록 해야한다. 

해결</br>
- @Modifying 어노테이션을 통해 해결이 가능했다. @Query와 같이 쓰인다. 벌크 연산을 할 때 주로 쓰이고 DELETE의 경우 한건이나 여러 건을 쿼리 메서드로 제공한다.
  
</div>
</details>

<details>
<summary> 이메일 인증 구현 중 에러 발생  </summary>
<div markdown="1">
  
문제</br>
- ![에러](https://github.com/JeinChoi/Shoppingmall/assets/59508337/e25f7739-0011-4351-acd9-ebb6c3a4e361)
- 이메일 인증 링크를 발송할 메일 계정을 설정하고 연결을 시도하던 중에 에러가 발생했다
    
원인</br>    
- 이 프로젝트 jdk 버전이 21인 점을 생각해 애초에 버전 문제이지 않을까 하는 추측을 통해 원인 파악을 하게 됐다.

해결</br>
- maven repository에서 spring-boot-starter-mail의 최신 버전을 찾아 수정했더니 해결하게 됐다.
  
</div>
</details>

<details>
<summary> RedisService bean이 여러개 생성되는 에러 발생 </summary>
<div markdown="1">
  
문제</br>
- bean 연결이 되지 않았다 
    
원인</br>    
- RedisService를 하나의 모듈에서만 쓰는게 아니라서 다른 모듈에도 bean 선언이 필요했고 그래서 여러 bean이 생성되어서 연결이 안됐던 것이다.

해결</br>
- @Primary annotation 선언으로 해결 했다. @Primary는 여러 bean이 있을 때 우선순위를 부여하는 어노테이션이다.
- Spring이 타입으로 bean을 찾다가 @Primary가 붙어있는 bean을 발견하면, 바로 해당 bean을 주입시킨다. 즉, @Primary는 여러 개의 bean들 중에서 우선순위를 부여하는 방법이다.
  
</div>
</details>

### ⏱️ 개발 기간
---
2024.04.17~
