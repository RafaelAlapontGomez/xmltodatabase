package com.rafael.xmltodatabase.processor;

import com.rafael.xmltodatabase.model.Items;
import com.rafael.xmltodatabase.model.PurchaseOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class PurchaseOrderProcessor implements ItemProcessor<PurchaseOrder, PurchaseOrder> {
    @Override
    public PurchaseOrder process(PurchaseOrder purchaseOrder) throws Exception {

        Items items = purchaseOrder.getItems();
        items.getItems().forEach( m -> m.setPurchaseOrderNumber(purchaseOrder.getPurchaseOrderNumber()));

        purchaseOrder.getAddresses().forEach( m -> m.setPurchaseOrderNumber(purchaseOrder.getPurchaseOrderNumber()));

        log.info(purchaseOrder.toString());
        purchaseOrder.getItems().getItems().forEach( m -> log.info(m.toString()));
        purchaseOrder.getAddresses().forEach( m -> log.info(m.toString()));

        return purchaseOrder;
    }
}
