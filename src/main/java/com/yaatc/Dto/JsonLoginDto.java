package com.yaatc.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
public class JsonLoginDto {


    @XmlElement
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 6, max = 40, message="email-size")
    @Email(message="email-pattern")
    private String email;

    @XmlElement
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 6, max = 30, message="password-size")
    private String password;

    public JsonLoginDto() {}

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
