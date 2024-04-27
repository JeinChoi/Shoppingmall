package com.shoppingmall.preorder.shopinfo;

import com.shoppingmall.preorder.domain.Item;
import com.shoppingmall.preorder.dto.ItemDetailDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Timestamp;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component // @RequiredArgsConstructor 와 함께 사용할 경우 스프링이 자동으로 생성
public class ShopInfoSearch {


    public String search() {
        String upcyclingshop = "신발";
        // https://openapi.naver.com/v1/search/shop.xml?query=%EC%A3%BC%EC%8B%9D&display=10&start=1&sort=sim
        URI uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com").path("/v1/search/shop")
                .queryParam("query", upcyclingshop).queryParam("display", 50).queryParam("start", 1).queryParam("sort", "sim")
                .encode().build().toUri();

        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> requestEntity = RequestEntity.get(uri).header("X-Naver-Client-Id", "WCYpJGoVkvYJFnuRhMzB")
                .header("X-Naver-Client-Secret", "zqAdjv_STY").build();
        ResponseEntity<String> result = restTemplate.exchange(requestEntity, String.class);


        String response = result.getBody();

        return response;
    }

//    public static void main(String[] args) {
//        ShopInfoSearch naverShopSearch = new ShopInfoSearch();
//        naverShopSearch.search();
//    }

    public List<Item> fromJSONtoItems(String result)  {
        // 문자열 정보를 JSONObject로 바꾸기
        JSONObject rjson = new JSONObject(result);
        System.out.println(rjson);
        // JSONObject에서 items 배열 꺼내기
        // JSON 배열이기 때문에 보통 배열이랑 다르게 활용해야한다.
        JSONArray items = rjson.getJSONArray("items");
        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            JSONObject itemJson = (JSONObject) items.get(i);
            Item item = new Item(
                    itemJson.getString("title"),//itemName
                    Integer.parseInt(itemJson.getString("lprice")),//price
                    10,//stockQuantity
                    itemJson.getString("brand"),//detail(brand)
                    "ON_SALE"//itemStateName
                    );
            itemList.add(item);

        }
        return itemList;
    }
}
