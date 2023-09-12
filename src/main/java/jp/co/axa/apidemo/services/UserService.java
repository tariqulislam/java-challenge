package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.User;
import jp.co.axa.apidemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean findUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null;
    }
    public boolean saveUser(User user) {
        final String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encryptedPassword);
        try{
            userRepository.save(user);
            return true;
        } catch (JpaSystemException ex) {
            return false;
        }
    }
}
