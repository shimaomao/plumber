package com.hebaibai.admin;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author MrBird
 */
@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
public class AdminApplication {

    private static final List<String> TYPE_ALIASES_PACKAGE = Arrays.asList(
            "com.hebaibai.admin.system.entity",
            "com.hebaibai.admin.plumber.entity",
            "com.hebaibai.admin.generator.entity"
    );

    @Bean
    MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.hebaibai.admin.*.mapper");
        return mapperScannerConfigurer;
    }

    @Bean
    MybatisSqlSessionFactoryBean sqlSessionFactory(@Autowired DataSource dataSource) throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        MybatisSqlSessionFactoryBean sessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mapper/*/*.xml"));
        sessionFactoryBean.setTypeAliasesPackage(String.join(",", TYPE_ALIASES_PACKAGE));
        sessionFactoryBean.setPlugins(new Interceptor[]{new PaginationInterceptor()});
        return sessionFactoryBean;
    }

    @Bean
    DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/admin?useUnicode=true&characterEncoding=utf-8");
        dataSource.setUsername("root");
        dataSource.setPassword("00000");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        return dataSource;
    }

    @Bean
    ExecutorService  executorService(){
        return Executors.newFixedThreadPool(16);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(AdminApplication.class).run(args);
    }

}
