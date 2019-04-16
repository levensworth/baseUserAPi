package com.yaatc.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yaatc.Controller.UserController;
import com.yaatc.Entity.User;
import org.springframework.web.util.UriBuilder;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

@XmlType(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDto extends EntityDto{

    private String name;

    private String surname;

    private String email;

    private Date birthday;
    

    @XmlElement
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 6, max = 30, message="password-size")
    private String password;

    public UserDto(){}

    public UserDto(User user) {
        super(user.getId());
        this.name = user.getName();
        this.surname = user.getSurname();
        this.email = user.getEmail();
        this.birthday = user.getBirthday();
        this.password = user.getPassword();
    }
    public UserDto(User user, UriBuilder baseUri) {
        super(user.getId());
        this.birthday = user.getBirthday();
        this.email = user.getEmail();
        this.password = user.getPassword();
//        this.userUrl = baseUri.clone()
//                .path(UserController.END_POINT)
//                .path(Long.toString(user.getId()))
//                .build().toString();

    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getPassword() {
        return password;
    }
}
