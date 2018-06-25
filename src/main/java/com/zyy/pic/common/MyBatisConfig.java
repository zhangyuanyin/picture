package com.zyy.pic.common;

/**
 * Created by Administrator on 2017/12/19.
 */
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.zyy.pic.common.datasource.DatabaseType;
import com.zyy.pic.common.datasource.DynamicDataSource;

/**
 * springboot集成mybatis的基本入口
 * 1）创建数据源(如果采用的是默认的tomcat-jdbc数据源，则不需要)
 * 2）创建SqlSessionFactory 3）配置事务管理器，除非需要使用事务，否则不用配置
 */
@Configuration
@MapperScan(basePackages = "com.gomefinance.pic.mapper")
public class MyBatisConfig implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    @Autowired
    private Environment env;

    /**
     * 创建数据源(数据源的名称：方法名可以取为XXXDataSource(),XXX为数据库名称,该名称也就是数据源的名称)
     */
    @Bean
    @ConfigurationProperties("db.fen")
    public DataSource fenDataSource() throws Exception {
//        Properties props = new Properties();
//        props.put("driverClassName", env.getProperty("jdbc.driverClassName"));
//        props.put("url", env.getProperty("jdbc.url"));
//        props.put("username", env.getProperty("jdbc.username"));
//        props.put("password", env.getProperty("jdbc.password"));
//        return DruidDataSourceFactory.createDataSource(props);
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("db.jie")
    public DataSource jieDataSource() throws Exception {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("db.che")
    public DataSource cheDataSource() throws Exception {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("db.fang")
    public DataSource fangDataSource() throws Exception {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("db.pic")
    public DataSource picDataSource() throws Exception {
        return DataSourceBuilder.create().build();
    }

    /**
     * @Primary 该注解表示在同一个接口有多个实现类可以注入的时候，默认选择哪一个，而不是让@autowire注解报错
     * @Qualifier 根据名称进行注入，通常是在具有相同的多个类型的实例的一个注入（例如有多个DataSource类型的实例）
     */
    @Bean
    @Primary
    public DynamicDataSource dataSource(@Qualifier("fenDataSource") DataSource fenDataSource,
                                        @Qualifier("jieDataSource") DataSource jieDataSource,
                                        @Qualifier("cheDataSource") DataSource cheDataSource,
                                        @Qualifier("fangDataSource") DataSource fangDataSource,
                                        @Qualifier("picDataSource") DataSource picDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DatabaseType.fendb, fenDataSource);
        targetDataSources.put(DatabaseType.jiedb, jieDataSource);
        targetDataSources.put(DatabaseType.chedb, cheDataSource);
        targetDataSources.put(DatabaseType.fangdb, fangDataSource);
        targetDataSources.put(DatabaseType.picdb, picDataSource);

        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
        dataSource.setDefaultTargetDataSource(fenDataSource);// 默认的datasource设置为myTestDbDataSource

        return dataSource;
    }

    /**
     * 根据数据源创建SqlSessionFactory
     */
//    @Bean
//    public SqlSessionFactory sqlSessionFactory(DynamicDataSource ds) throws Exception {
//
//        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
//        fb.setDataSource(ds);// 指定数据源(这个必须有，否则报错)
//        // 下边两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
////        fb.setTypeAliasesPackage(env.getProperty("mybatis.typeAliasesPackage"));// 指定基包
////        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis.mapperLocations")));//
//        return fb.getObject();
//    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("fenDataSource") DataSource fenDataSource,
                                               @Qualifier("jieDataSource") DataSource jieDataSource,
                                               @Qualifier("cheDataSource") DataSource cheDataSource,
                                               @Qualifier("fangDataSource") DataSource fangDataSource,
                                               @Qualifier("picDataSource") DataSource picDataSource) throws Exception{
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(this.dataSource(fenDataSource, jieDataSource,cheDataSource,fangDataSource,picDataSource));
//        fb.setTypeAliasesPackage(env.getProperty("mybatis.typeAliasesPackage"));
//        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis.mapperLocations")));
        return fb.getObject();
    }

    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DynamicDataSource ds) throws Exception {
        return new DataSourceTransactionManager(ds);
    }


}
