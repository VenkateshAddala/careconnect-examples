package uk.nhs.careconnect.fhirserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by kevinmayfield on 21/07/2017.
 */

@Configuration
@EnableTransactionManagement()
@PropertySource("classpath:HAPIJPA.properties")
@ComponentScan(basePackages = "uk.gov.hscic")
public class Config {

    @Value("${datasource.cleardown.cron:0 19 21 * * *}")
    private String cron;
}
