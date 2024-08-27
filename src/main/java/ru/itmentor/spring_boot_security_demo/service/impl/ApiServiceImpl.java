package ru.itmentor.spring_boot_security_demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.itmentor.spring_boot_security_demo.entity.Role;
import ru.itmentor.spring_boot_security_demo.entity.User;
import ru.itmentor.spring_boot_security_demo.service.ApiService;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ApiServiceImpl implements ApiService {

    private static final String URL = "http://94.198.50.185:7081/api/users";
    private final RestTemplate restTemplate;
    private String sessionId;


    @Override
    public void performApiOperations() {
        // 1. Получение списка всех пользователей
        ResponseEntity<String> response = getAllUsers();
        sessionId = extractSessionId(response);
        System.out.println("Session ID: " + sessionId);

        // 2. Сохранение нового пользователя
        User user = new User(3, "James", "Brown", 30);
        user.setRoles(Set.of(Role.ROLE_USER));
        String part1 = saveUser(user);
        System.out.println("Part 1: " + part1);

        // 3. Изменение пользователя
        user.setName("Thomas");
        user.setLastName("Shelby");
        String part2 = updateUser(user);
        System.out.println("Part 2: " + part2);

        // 4. Удаление пользователя
        String part3 = deleteUser(3);
        System.out.println("Part 3: " + part3);

        // Итоговый код
        String finalCode = part1 + part2 + part3;
        System.out.println("Final code: " + finalCode);
    }


    private ResponseEntity<String> getAllUsers() {
        return restTemplate.exchange(URL, HttpMethod.GET, null, String.class);
    }

    private String extractSessionId(ResponseEntity<String> response) {
        HttpHeaders headers = response.getHeaders();
        List<String> cookies = headers.get("Set-Cookie");
        if (cookies != null && !cookies.isEmpty()) {
            return cookies.get(0).split(";")[0];
        }
        return null;
    }

    private String saveUser(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", sessionId);

        HttpEntity<User> request = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, request, String.class);
        return response.getBody();
    }

    private String updateUser(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", sessionId);

        HttpEntity<User> request = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.PUT, request, String.class);
        return response.getBody();
    }

    private String deleteUser(int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", sessionId);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(URL + "/" + id, HttpMethod.DELETE, request, String.class);
        return response.getBody();
    }
}