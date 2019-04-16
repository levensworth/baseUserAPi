package com.yaatc.Controller;

import com.yaatc.Dto.UserDto;
import com.yaatc.Entity.User;
import com.yaatc.Service.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

import static com.yaatc.Controller.UserController.END_POINT;

@RestController
@RequestMapping(END_POINT)
public class UserController {

    public static final String END_POINT = "users";

    @Autowired
    private UserServiceImp userService;

    @GetMapping
    public User login(@PathParam("id") final long id) {

        return userService.findById(id).orElseThrow(RuntimeException::new);
    }

    @PostMapping
    public  User singUp(@RequestBody UserDto user) {
        return userService.create(user.getName(), user.getSurname(), user.getEmail(),
                user.getBirthday(), user.getPassword());
    }
    
}

