package com.rafael.xmltodatabase.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder @ToString
@XStreamAlias("Address")
public class Address {

    private Integer purchaseOrderNumber;

    @XStreamAsAttribute
    @XStreamAlias("Type")
    private String type;

    @XStreamAlias("Name")
    private String name;

    @XStreamAlias("Street")
    private String street;

    @XStreamAlias("City")
    private String city;

    @XStreamAlias("State")
    private String state;

    @XStreamAlias("Zip")
    private String zip;

    @XStreamAlias("Country")
    private String country;
}
