package org.prgms.kdt.customer;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringJUnitConfig
class CustomerJdbcRepositoryTest {
    @Configuration
    @ComponentScan(basePackages = {"org.prgms.kdt.customer"})
    static class Config{

        @Bean
        public DataSource dataSource(){
            HikariDataSource dataSource = DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost/order_mgmt")
                    .username("root")
                    .password("hi01071104")
                    .type(HikariDataSource.class)
                    .build();
            dataSource.setMaximumPoolSize(1000);
            dataSource.setMinimumIdle(100);
            return dataSource;
        }
    }

    @Autowired
    CustomerJdbcRepository customerJdbcRepository;

    @Autowired
    DataSource dataSource;

    @Test
    public void testHikariConnectionPool(){
        assertThat(dataSource.getClass().getName(), is("com.zaxxer.hikari.HikariDataSource"));
    }

    @Test
    @DisplayName("전체 고객을 조회할 수 있다.")
    public void testFindAll() {
        var customers=customerJdbcRepository.findAll();
        assertThat(customers.isEmpty(), is(false));
    }

    @Test
    @DisplayName("이름으로 고객을 조회할 수 있다.")
    public void testFindByName() {
        var customer=customerJdbcRepository.findByName("new-user");
        assertThat(customer.isEmpty(), is(false));

        var unknown=customerJdbcRepository.findByName("unknown-user");
        assertThat(unknown.isEmpty(), is(true));
    }

    @Test
    @DisplayName("이메일로 고객을 조회할 수 있다.")
    public void testFindByEmail(){
        var customer=customerJdbcRepository.findByEmail("new-user@gmail.com");
        assertThat(customer.isEmpty(), is(false));

        var unknown=customerJdbcRepository.findByEmail("unknown-user@gmail.com");
        assertThat(unknown.isEmpty(), is(true));
    }

    @Test
    @DisplayName("고객을 추가할 수 있다.")
    public void testInsert() {
        customerJdbcRepository.deleteAll();

        var newCustomer=new Customer(UUID.randomUUID(),"test-user","test-user@gmail.com", LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        customerJdbcRepository.insert(newCustomer);

        var retrievedCustomer = customerJdbcRepository.findById(newCustomer.getCustomerId());
        assertThat(retrievedCustomer.isEmpty(), is(false));
        assertThat(retrievedCustomer.get(), samePropertyValuesAs(newCustomer));

    }
}