//package com.shoppingmall.apigateway.filter;
//
//import com.example.project.dto.CustomUserDetails;
//import com.example.project.entity.UserEntity;
//import com.shoppingmall.apigateway.security.JwtUtil;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.server.WebFilterChain;
//
//import java.io.IOException;
//
//@RequiredArgsConstructor
//public class JWTFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//
//    @Override //jwtutil을 통해서 검증
//    protected void doFilterInternal(ServerHttpRequest request, ServerHttpResponse response, WebFilterChain filterChain) throws ServletException, IOException {
//
//        //request에서 authorization 헤더를 찾음
//        String authorization = request.getHeader("Authorization");
//
//        if(authorization == null || !authorization.startsWith("Bearer ")){
//            System.out.println("token null");
//            filterChain.doFilter(request,response);
//            //filterchain에 속한 다른 필터에 request,response를 넘겨주면 됨
//
//        }
//
//        String token = authorization.split(" ")[1];
//
//        //토큰 소멸 시간 검증
//        if(jwtUtil.isExpired(token)){
//            System.out.println("token expired");
//            filterChain.doFilter(request,response);
//
//            //조건이 해당되면 메서드 종료 (필수)
//            return;
//        }
//
//        String username = jwtUtil.getUsername(token);
//        String role = jwtUtil.getRole(token);
//
//        UserEntity userEntity =  new UserEntity();
//        userEntity.setUsername(username);
//        userEntity.setPassword("temppassword");//토큰에 비밀번호는 없기 때문에 임시값으로 저장
//        userEntity.setRole(role);
//
//        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
//        Authentication authToken= new UsernamePasswordAuthenticationToken(customUserDetails,null,customUserDetails.getAuthorities());
//
//        SecurityContextHolder.getContext().setAuthentication(authToken);
//
//        filterChain.doFilter(request,response);
//    }
//}
