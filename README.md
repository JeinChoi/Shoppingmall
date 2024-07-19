
### 📢 프로젝트 소개

---


- 고객이 상품을 구매하고, 구매함에 따라 재고 관리, 배송 관리를 해주는 서비스입니다.

- MSA 서비스이기 때문에 개별 배포가 가능하고 장애 격리가 수월합니다.

</br>

### 🏞 개발 환경

---

![java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![spring boot](https://img.shields.io/badge/-Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![spring cloud](https://img.shields.io/badge/-Spring%20Cloud-6DB33F?style=for-the-badge&logo=icloud&logoColor=white)
![spring security](https://img.shields.io/badge/-Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![My SQL](https://img.shields.io/badge/-My%20SQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/-redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)

- Version: Java 21

- Framework: SpringBoot 3.2.2

- Database: MySQL 8.0

- ORM: JPA/Hibernate
</br>

### 🏠 아키텍처
---
![아키텍처](https://github.com/user-attachments/assets/5811530a-6029-4e51-848f-620a6c44a573)


</br>

### ⭐ 주요 기능
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


### 🖋 ERD
---
![ERD](https://github.com/JeinChoi/Shoppingmall/assets/59508337/b424afbb-e6ee-4f59-a1ba-9221f38cf5c3)

</br>


### ⏱️ 개발 기간
---
2024.04.17~
