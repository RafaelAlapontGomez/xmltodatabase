package com.rafael.xmltodatabase.components;

import com.rafael.xmltodatabase.model.Address;
import com.rafael.xmltodatabase.model.Item;
import com.rafael.xmltodatabase.model.PurchaseOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class PurchaseOrderWriter implements ItemStreamWriter<PurchaseOrder>, ItemWriter<PurchaseOrder> {

    private static final String QUERY_INSERT_PURCHASED = "INSERT " +
            "INTO public.\"PurchaseOrder\"(purchaseOrderNumber, orderDate, deliveryNotes) " +
            "VALUES (:purchaseOrderNumber, :orderDate, :deliveryNotes)";

    private static final String QUERY_INSERT_ADDRESS = "INSERT " +
            "INTO public.\"Address\"(purchaseOrderNumber, name, street, city, state, zip, country) " +
            "VALUES (:purchaseOrderNumber, :name, :street, :city, :state, :zip, :country)";

    private static final String QUERY_INSERT_ITEM = "INSERT " +
            "INTO public.\"Item\"(purchaseOrderNumber, partNumber, productName, quantity, uSPrice, shipDate, comment) " +
            "VALUES (:purchaseOrderNumber, :partNumber, :productName, :quantity, :uSPrice, :shipDate, :comment)";

    private OutputStream outputStream;

    @Autowired
    NamedParameterJdbcTemplate postgresJdbcTemplate;

    @Autowired
    DataSource dataSource;

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void close() throws ItemStreamException {

    }

    @Override
    public void write(List<? extends PurchaseOrder> pruchaseOrders) throws Exception {

        log.info(QUERY_INSERT_PURCHASED);
        log.info(QUERY_INSERT_ADDRESS);
        log.info(QUERY_INSERT_ITEM);

        for (PurchaseOrder purchaseOrder: pruchaseOrders) {
            log.info( pruchaseOrders.toString() );
            Map<String, Object> paramMap = purchaseOrderParamMap(purchaseOrder);

            postgresJdbcTemplate.update(QUERY_INSERT_PURCHASED, paramMap);

            insertAddress(purchaseOrder.getAddresses());
            insertItem(purchaseOrder.getItems().getItems());

        }

    }

    private void insertItem(List<Item> items) {

        for(Item item: items) {
            log.info(item.toString());

            Map<String, Object> paramMap = itemParamMap(item);
            postgresJdbcTemplate.update(QUERY_INSERT_ITEM, paramMap);
        }
    }

    private Map<String, Object> itemParamMap(Item item) {
        Map<String, Object> paramMap = new HashMap<>();

        paramMap.put("purchaseOrderNumber", item.getPurchaseOrderNumber());
        paramMap.put("partNumber", item.getPartNumber());
        paramMap.put("productName", item.getProductName());
        paramMap.put("quantity", item.getQuantity());
        paramMap.put("uSPrice", item.getUSPrice());
        paramMap.put("shipDate", item.getShipDate());
        paramMap.put("comment", item.getComment());

        return paramMap;
    }

    private void insertAddress(List<Address> addresses) {

        for (Address address: addresses) {
            log.info(address.toString());

            Map<String, Object> paramMap = addressParamMap(address);
            postgresJdbcTemplate.update(QUERY_INSERT_ADDRESS, paramMap);
        }
    }

    private Map<String, Object> addressParamMap(Address address) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("purchaseOrderNumber", address.getPurchaseOrderNumber());
        paramMap.put("name", address.getName());
        paramMap.put("city", address.getCity());
        paramMap.put("state", address.getState());
        paramMap.put("zip", address.getZip());
        paramMap.put("country", address.getCountry());
        paramMap.put("street", address.getStreet());

        return paramMap;
    }

    private Map<String, Object> purchaseOrderParamMap(PurchaseOrder purchaseOrder) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("purchaseOrderNumber", purchaseOrder.getPurchaseOrderNumber());
        paramMap.put("orderDate", purchaseOrder.getOrderDate());
        paramMap.put("deliveryNotes", purchaseOrder.getDeliveryNotes());

        return paramMap;
    }
}
