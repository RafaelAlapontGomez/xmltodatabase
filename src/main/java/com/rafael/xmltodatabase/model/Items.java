package com.rafael.xmltodatabase.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.*;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder @ToString
@XStreamAlias("Items")
public class Items {

    @XStreamImplicit
    @XStreamAlias("Item")
    private List<Item> items;
}
