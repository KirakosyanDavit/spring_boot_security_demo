package ru.itmentor.spring_boot_security_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmentor.spring_boot_security_demo.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);



}
