package com.yaatc.Service;

import com.yaatc.Entity.User;
import com.yaatc.Interface.Persistance.UserDao;
import com.yaatc.Interface.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class UserServiceImp implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User create(String name, String surname, String email, Date birthday, String password) {
        // TODO: encrypt password
        return userDao.create(name.toUpperCase(), surname.toUpperCase(), email.toUpperCase(), birthday, passwordEncoder.encode(password));
    }

    public Optional<User> findById(final long id) {
            return userDao.findById(id);
        }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public boolean changeUser(User user) {
        boolean changes = false;
        changes = changes && userDao.changeName(user.getId(), user.getName());
        changes = changes && userDao.changeSurname(user.getId(), user.getSurname());
        changes = changes && userDao.changeEmail(user.getId(), user.getEmail());
        changes = changes && userDao.changeBirthday(user.getId(), user.getBirthday());
        return changes;
    }

}
