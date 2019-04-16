package com.yaatc.Interface.Service;

import com.yaatc.Entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Transactional
public interface UserService {

    User create(String name, String surname, String email, Date birthday, String password);

    Optional<User> findById(final long id);

    Optional<User> findByEmail(final String email);

    boolean changeUser(User user);

}
