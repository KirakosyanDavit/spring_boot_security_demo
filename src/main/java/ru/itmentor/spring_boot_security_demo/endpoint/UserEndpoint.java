package ru.itmentor.spring_boot_security_demo.endpoint;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring_boot_security_demo.entity.User;
import ru.itmentor.spring_boot_security_demo.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserEndpoint {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> userAll(@AuthenticationPrincipal User user) {
        List<User> allUsers = userService.getAllUsers();
        Optional<User> roleUser = allUsers.stream()
                .filter(roles ->
                        roles.getRoles()
                                .stream()
                                .anyMatch(role ->
                                        role.name().equals("ROLE_USER")) &&
                                roles.getId().equals(user.getId())).findFirst();

        List<User> users = roleUser.map(Collections::singletonList)
                .orElse(allUsers);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") int id, @RequestBody User user) {
        User body = userService.updateUser(id, user);
        if (body == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable("id") int id) {
        User body = userService.deleteUserById(id);
        if (body == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

}
