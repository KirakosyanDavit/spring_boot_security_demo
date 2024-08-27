package ru.itmentor.spring_boot_security_demo.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmentor.spring_boot_security_demo.dto.UserAuthResponseDto;
import ru.itmentor.spring_boot_security_demo.entity.User;
import ru.itmentor.spring_boot_security_demo.service.UserService;
import ru.itmentor.spring_boot_security_demo.jwt_token.JwtTokenUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginAndRegisterEndpoint {


    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<UserAuthResponseDto> login(@RequestBody User user){

        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        if (!passwordEncoder.matches(user.getPassword(), userDetails.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = jwtTokenUtil.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(new UserAuthResponseDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User saveUser = userService.saveUser(user);
        if (saveUser == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(saveUser);
    }
}
