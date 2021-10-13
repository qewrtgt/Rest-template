package com.springboot.resttemplate;

import com.springboot.resttemplate.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@SpringBootApplication
public class RestTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestTemplateApplication.class, args);

        final String URL = "http://91.241.64.178:7081/api/users";
        final StringBuilder stringBuilder = new StringBuilder();

        // HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // HttpEntity<String>: получить результат в виде строки
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Отправить запрос с помощью метода GET and Headers.
        ResponseEntity<String> responseGetAll = restTemplate.exchange(URL, HttpMethod.GET, entity, String.class);
        System.out.println(responseGetAll.toString());

        //Получаем set-cookie
        String cookies = responseGetAll.getHeaders().getFirst("Set-Cookie");


        //Устанавливаем куки и header для всех запросов RestTemplate
        restTemplate.getInterceptors()
                .add((request, body, execution) -> {
                    request.getHeaders().set("Cookie", cookies);
                    return execution.execute(request, body);
                });

        User userNew = new User(3l, "James", "Brown", (byte) 100);
        HttpEntity<User> userNewStr = new HttpEntity<>(userNew, headers);
        ResponseEntity<String> resultAddUser = restTemplate.exchange(URL, HttpMethod.POST, userNewStr, String.class);
        System.out.println(resultAddUser.getBody());

        User userEdit = new User(3l, "Thomas", "Shelby", (byte) 100);
        HttpEntity<User> userEditStr = new HttpEntity<>(userEdit, headers);
        ResponseEntity<String> resultEditUser = restTemplate.exchange(URL, HttpMethod.PUT, userEditStr, String.class);
        System.out.println(resultEditUser.getBody());

        ResponseEntity<String> resultDeleteUser = restTemplate.exchange(URL + "/3", HttpMethod.DELETE, userEditStr, String.class);
        System.out.println(resultDeleteUser.getBody());

        stringBuilder.append(resultAddUser.getBody())
                .append(resultEditUser.getBody())
                .append(resultDeleteUser.getBody());
        System.out.println(stringBuilder);
    }
}














