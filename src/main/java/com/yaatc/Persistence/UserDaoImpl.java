package com.yaatc.Persistence;

import com.yaatc.Entity.User;
import com.yaatc.Interface.Persistance.UserDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public User create(String name, String surname, String email, Date birthday, String password) {
        User user = new User(name, surname, email, new Date(), password);
        entityManager.persist(user);
        return user;
    }

    @Override
    public Optional<User> findById(long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("FROM User AS us WHERE  UPPER(us.email) = :email ",
                User.class);
        query.setParameter("email", email.toUpperCase());
        return query.getResultList().stream().findFirst();
    }

    @Override
    public boolean changeName(long id, String newName) {
        Optional<User> optionalUser = findById(id);
        optionalUser.ifPresent(user -> {
            user.setName(newName);
            entityManager.persist(user);
        });
        return  true;
    }

    @Override
    public boolean changeSurname(long id, String newSurname) {
        Optional<User> optionalUser = findById(id);
        optionalUser.ifPresent(user -> {
            user.setSurname(newSurname);
            entityManager.persist(user);
        });
        return true;
    }

    @Override
    public boolean changeEmail(long id, String newEmail) {
        Optional<User> optionalUser = findById(id);
        optionalUser.ifPresent(user -> {
            user.setEmail(newEmail);
            entityManager.persist(user);
        });
        return true;
    }

    @Override
    public boolean changeBirthday(long id, Date newBirthday) {
        Optional<User> optionalUser = findById(id);
        optionalUser.ifPresent(user -> {
            user.setBirthday(newBirthday);
            entityManager.persist(user);
        });
        return true;
    }
}
