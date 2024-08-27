package ru.itmentor.spring_boot_security_demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.itmentor.spring_boot_security_demo.entity.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    User saveUser(User user);

    User updateUser(int id, User userUpdate);

    User deleteUserById(int id);

    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    User getById(int id);
}
