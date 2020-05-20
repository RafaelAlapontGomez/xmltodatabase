package com.rafael.xmltodatabase.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Data @NoArgsConstructor @AllArgsConstructor @Builder @ToString
@XStreamAlias("Item")
public class Item {

    private Integer purchaseOrderNumber;

    @XStreamAsAttribute
    @XStreamAlias("PartNumber")
    private String partNumber;

    @XStreamAlias("ProductName")
    private String productName;

    @XStreamAlias("Quantity")
    private Integer quantity;

    @XStreamAlias("USPrice")
    private Double uSPrice;

    @XStreamAlias("Comment")
    private String comment;

    @XStreamAlias("ShipDate")
    private Date shipDate;
}
