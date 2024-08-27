package ru.itmentor.spring_boot_security_demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmentor.spring_boot_security_demo.entity.User;
import ru.itmentor.spring_boot_security_demo.repository.UserRepository;
import ru.itmentor.spring_boot_security_demo.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
        Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());
        if (userByEmail.isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setToken(UUID.randomUUID().toString());
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public User updateUser(int id, User userUpdate) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            if (!user.getEmail().equals(userUpdate.getEmail())) {
                Optional<User> userByEmail = userRepository.findByEmail(userUpdate.getEmail());
                if (userByEmail.isPresent()) {
                    user.setName(userUpdate.getName());
                    user.setLastName(userUpdate.getLastName());
                    user.setAge(userUpdate.getAge());
                    user.setGender(userUpdate.getGender());
                    user.setEmail(userUpdate.getEmail());
                    user.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
                    user.setRoles(userUpdate.getRoles());
                    return userRepository.save(user);
                } else {
                    System.out.println("User email is Null");
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public User deleteUserById(int id) {
        Optional<User> byId = userRepository.findById(id);
        byId.ifPresent(user -> userRepository.deleteById(user.getId()));
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    }

    @Override
    public User getById(int id) {
        Optional<User> byId = userRepository.findById(id);
        return byId.orElse(null);
    }


}
