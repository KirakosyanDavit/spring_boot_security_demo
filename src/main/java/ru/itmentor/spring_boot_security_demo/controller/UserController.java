package ru.itmentor.spring_boot_security_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring_boot_security_demo.entity.User;
import ru.itmentor.spring_boot_security_demo.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public String getAllUser(ModelMap modelMap, @AuthenticationPrincipal User user) {

        List<User> allUsers = userService.getAllUsers();

        Optional<User> roleUser = allUsers.stream()
                .filter(roles ->
                        roles.getRoles()
                                .stream()
                                .anyMatch(role ->
                                        role.name().equals("ROLE_USER")) &&
                                roles.getId().equals(user.getId()))
                .findFirst();

        if (roleUser.isPresent()) {
            modelMap.addAttribute("users", roleUser.get());
            return "user";
        } else {
            modelMap.addAttribute("users", allUsers);
            return "user";
        }
    }

    @GetMapping("/update/{id}")
    public String updateUser(@PathVariable("id") int id, ModelMap modelMap) {
        modelMap.addAttribute("user", userService.getById(id));
        return "update";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") int id, @ModelAttribute User user) {
        userService.updateUser(id, user);
        return "redirect:/user";
    }

    @GetMapping("/delete/{id}")
    public String deleteUserById(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        return "redirect:/user";
    }

}
