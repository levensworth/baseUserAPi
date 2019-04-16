package com.yaatc.Interface.Persistance;

import com.yaatc.Entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Transactional
public interface UserDao {

    User create(String name, String surname, String email, Date birthday, String password);

    Optional<User> findById(final long id);

    Optional<User> findByEmail(final String email);

    boolean changeName(final long id, String newName);

    boolean changeSurname(final long id, String newSurname);

    boolean changeEmail(final long id, String newEmail);

    boolean changeBirthday(final long id, Date newBirthday);

}
