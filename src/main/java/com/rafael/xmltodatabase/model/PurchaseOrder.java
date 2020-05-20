package com.rafael.xmltodatabase.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder @ToString
@XStreamAlias("PurchaseOrder")
public class PurchaseOrder {

    @XStreamAsAttribute
    @XStreamAlias("PurchaseOrderNumber")
    private Integer purchaseOrderNumber;

    @XStreamAsAttribute
    @XStreamAlias("OrderDate")
    private Date orderDate;

    @XStreamImplicit
    @XStreamAlias("Address")
    private List<Address> addresses;

    @XStreamAlias("DeliveryNotes")
    private String deliveryNotes;

    @XStreamAlias("Items")
    private Items items;
}
