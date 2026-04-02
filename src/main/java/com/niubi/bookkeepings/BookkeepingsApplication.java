package com.niubi.bookkeepings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@EnableTransactionManagement
@SpringBootApplication
public class BookkeepingsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookkeepingsApplication.class, args);
    }

}
