package jp.co.axa.apidemo.service;

import static org.assertj.core.api.Assertions.assertThat;

import jp.co.axa.apidemo.entities.User;
import jp.co.axa.apidemo.repositories.UserRepository;
import jp.co.axa.apidemo.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    private  UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Test

    public void  userShouldFindByEmail() {
        userRepository.deleteAll();
        User expectedUser = new User();
        expectedUser.setEmail("tariqul@gmail.com");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode("admin123333");
        expectedUser.setPassword(encodePassword);
        User savedUser = userRepository.save(expectedUser);

        boolean isFindEmail = userService.findUserByEmail(savedUser.getEmail());
        assertThat(isFindEmail).isTrue();
    }


}
