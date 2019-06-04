package com.mywuwu.flowable;


import com.mywuwu.config.ApplicationConfiguration;
import com.mywuwu.servlet.AppDispatcherServletConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import({
        ApplicationConfiguration .class,
        AppDispatcherServletConfiguration.class
})
@SpringBootApplication
@ComponentScan(basePackages = {"com.mywuwu"})
@EnableTransactionManagement
public class MywuwuFlowableApplication {

    public static void main(String[] args) {
        SpringApplication.run(MywuwuFlowableApplication.class, args);
    }

}
