package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.User;
import jp.co.axa.apidemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/* User Service implementation
* Handling the logic for login and register user*/
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /* Service Method for find the user by email
    * Using for validate the user*/
    public boolean findUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null;
    }

    /* Service method for save user.
    * user for register the user in application */
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
