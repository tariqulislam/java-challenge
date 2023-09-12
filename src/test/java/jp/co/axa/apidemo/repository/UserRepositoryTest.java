package jp.co.axa.apidemo.repository;

import static org.assertj.core.api.Assertions.assertThat;
import jp.co.axa.apidemo.entities.User;
import jp.co.axa.apidemo.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureBefore(CacheAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration
@WebAppConfiguration
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName(value = "User Registration Test")
    void shouldUserCanRegister() {
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
    @DisplayName(value = "User Find Test")
    void shouldUserCanFindByEmail() {
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
