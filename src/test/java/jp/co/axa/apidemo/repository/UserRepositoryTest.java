package jp.co.axa.apidemo.repository;

import static org.assertj.core.api.Assertions.assertThat;
import jp.co.axa.apidemo.entities.User;
import jp.co.axa.apidemo.repositories.UserRepository;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;



@DataJpaTest
@AutoConfigureBefore(CacheAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration
@WebAppConfiguration
@RunWith(SpringRunner.class)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldUserCanRegister() {
        User expectedUser = new User();
        expectedUser.setEmail("tariqul@eeeee.com");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode("admin123333");
        expectedUser.setPassword(encodePassword);
        User savedUser = userRepository.save(expectedUser);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);
        assertThat(savedUser.getUsername()).isNotEmpty();
    }

    @Test
   public void shouldUserCanFindByEmail() {
        User expectedUser = new User();
        expectedUser.setEmail("tariqul@eeeee.com");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode("admin123333");
        expectedUser.setPassword(encodePassword);
        User savedUser = userRepository.save(expectedUser);
        User userFindByEmail = userRepository.findByEmail(savedUser.getEmail()).orElse(null);
        assertThat(userFindByEmail).isNotNull();
    }
}
