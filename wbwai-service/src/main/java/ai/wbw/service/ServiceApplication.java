package ai.wbw.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@MapperScan(basePackages = {"ai.wbw.service.core.mapper"}) //扫描mapper
public class ServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(ServiceApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意Application是启动类，就是main方法所属的类
        return builder.sources(ServiceApplication.class);
    }
}
