package com.rafael.xmltodatabase.config;

import com.rafael.xmltodatabase.components.PurchaseOrderWriter;
import com.rafael.xmltodatabase.model.Address;
import com.rafael.xmltodatabase.model.Item;
import com.rafael.xmltodatabase.model.PurchaseOrder;
import com.rafael.xmltodatabase.processor.PurchaseOrderProcessor;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    NamedParameterJdbcTemplate odbcTemplate;

    @Autowired
    private PurchaseOrderWriter purchaseOrderWriter;

    @Bean
    public PurchaseOrderProcessor processor() {
        return new PurchaseOrderProcessor();
    }




    @Bean
    public StaxEventItemReader<PurchaseOrder> reader(){
        StaxEventItemReader<PurchaseOrder> reader = new StaxEventItemReader<PurchaseOrder>();
        reader.setResource(new ClassPathResource("PurchaseOrder.xml"));
        reader.setFragmentRootElementName("PurchaseOrder");

        XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
        xStreamMarshaller.setAnnotatedClasses(PurchaseOrder.class);

        xStreamMarshaller.setAutodetectAnnotations(true);
        xStreamMarshaller.getXStream().registerConverter(
                new com.thoughtworks.xstream.converters.basic.DateConverter("yyyy-MM-dd HH:mm", new String[] {"yyyy-MM-dd"},new GregorianCalendar().getTimeZone()){
                    public boolean canConvert(Class type) {
                        return type.equals(Date.class);
                    }
                    public String toString(Object obj) {
                        return new SimpleDateFormat("yyyy-MM-dd").format((Date) obj);
                    }
                },  XStream.PRIORITY_NORMAL);

        reader.setUnmarshaller(xStreamMarshaller);

        return reader;
    }

    public CompositeItemWriter<PurchaseOrder> compositeItemWriter(){
        CompositeItemWriter writer = new CompositeItemWriter();
        writer.setDelegates(Arrays.asList(purchaseOrderWriter));
        return writer;
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<PurchaseOrder, PurchaseOrder>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(compositeItemWriter())
                .stream(this.purchaseOrderWriter)
                .build();
    }

    @Bean
    public Job importUserJob() {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();

    }
}
