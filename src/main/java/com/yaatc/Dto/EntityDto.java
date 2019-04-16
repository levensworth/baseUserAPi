package com.yaatc.Dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlRootElement(name = "")
public class EntityDto {

    private Long id;

    public EntityDto(Long id) {
        this.id = id;
    }

    public EntityDto() {}

    /**
     * This method is annotated with the {@link XmlElement} annotation in order to let SubClasses
     * override the property when overriding the method.
     */
    @XmlElement(required = true)
    public Long getId() {
        return id;
    }
}
