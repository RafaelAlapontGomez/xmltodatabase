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
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
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
            "INTO public.\"PurchaseOrder\"(\"purchaseOrderNumber\", \"orderDate\", \"deliveryNotes\") " +
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
    public void write(List<? extends PurchaseOrder> purchaseOrders) throws Exception {

        log.info(QUERY_INSERT_PURCHASED);
        log.info(QUERY_INSERT_ADDRESS);
        log.info(QUERY_INSERT_ITEM);

        for (PurchaseOrder purchaseOrder: purchaseOrders) {
            log.info( purchaseOrders.toString() );

            SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(purchaseOrder);

            postgresJdbcTemplate.update(QUERY_INSERT_PURCHASED, namedParameters);

            insertAddress(purchaseOrder.getAddresses());
            insertItem(purchaseOrder.getItems().getItems());

        }

    }

    private void insertItem(List<Item> items) {

        for(Item item: items) {
            log.info(item.toString());
            SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(item);

            postgresJdbcTemplate.update(QUERY_INSERT_ITEM, namedParameters);
        }
    }

    private void insertAddress(List<Address> addresses) {

        for (Address address: addresses) {
            log.info(address.toString());

            SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(address);

            postgresJdbcTemplate.update(QUERY_INSERT_ADDRESS, namedParameters);
        }
    }
}
