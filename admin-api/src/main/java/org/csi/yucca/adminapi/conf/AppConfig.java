/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.conf;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.service.impl.MailServiceImpl;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

//import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableTransactionManagement
@EnableWebMvc
//@ComponentScan(basePackages = {
//		"org.csi.yucca.adminapi.conf",
//		"org.csi.yucca.adminapi.controller",
//		"org.csi.yucca.adminapi.delegate",
//		"org.csi.yucca.adminapi.jwt",
//		"org.csi.yucca.adminapi.messaging",
//		"org.csi.yucca.adminapi.service" 
//		})
@ComponentScan(basePackages = "org.csi.yucca.adminapi")
@MapperScan("org.csi.yucca.adminapi.mapper")
@PropertySource(value = { "classpath:datasource.properties" })
public class AppConfig extends WebMvcConfigurerAdapter {

	@Value("${driver.class.name}")
	private String driverClassName;

	@Value("${url}")
	private String url;

	@Value("${datasource.username}")
	private String username;

	@Value("${password}")
	private String password;

	@Value("${max.idle}")
	private int maxIdle;

	@Value("${max.active}")
	private int maxActive;
	

	// mail
	@Value("${mail.host}")
	private String mailHost;

	@Value("${mail.port}")
	private String mailPort;

	@Value("${mail.smtp.starttls.enable}")
	private String mailsmtpStarttlEnable;

	@Value("${mail.smtp.auth}")
	private String mailSmtpAuth;

	@Value("${mail.transport.protocol}")
	private String mailTransportProtocol;

	@Value("${mail.debug}")
	private String mailDebug;

	private static final Logger logger = Logger.getLogger(AppConfig.class);


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Bean
	public JavaMailSender getMailSender() {
		logger.info("[AppConfig::getMailSender] - START");
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
		/*
		
		<parameter name="mail.smtp.host">
			@@mail.smtp.host@@
		</parameter> 
		<parameter name="mail.smtp.port">
			@@mail.smtp.port@@
		</parameter> 
		<parameter name="mail.smtp.starttls.enable">
			@@mail.smtp.starttls.enable@@
		</parameter> 
		<parameter name="mail.smtp.auth">
			@@mail.smtp.auth@@
		</parameter> 

		<parameter 
		<a href="mailto:name="mail.smtp.user">
			@@mail.smtp.user@@
		</a>
		</parameter> 
		
		<parameter name="mail.smtp.password">
			mailpassword
		</parameter> 
		
		<parameter <a href="mailto:name="mail.smtp.from">claudio.parodi@csi.it">name="mail.smtp.from">claudio.parodi@csi.it</a></parameter>		
		
		*/
		
		
		// Using gmail
		mailSender.setHost(mailHost);
		
//		mailSender.setUsername("@@mailsender.username@@");
//		mailSender.setPassword("@@mailsender.password@@");
		
		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.starttls.enable", mailsmtpStarttlEnable);
		javaMailProperties.put("mail.smtp.auth", mailSmtpAuth);
		javaMailProperties.put("mail.transport.protocol", mailTransportProtocol);
		javaMailProperties.put("mail.debug", mailDebug);

		logger.info("[AppConfig::getMailSender] - mailTransportProtocol:" + mailTransportProtocol);

		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}

	// @Override
	// public void addResourceHandlers(ResourceHandlerRegistry registry) {
	// registry.addResourceHandler("swagger-ui.html")
	// .addResourceLocations("classpath:/META-INF/resources/");
	//
	// registry.addResourceHandler("/webjars/**")
	// .addResourceLocations("classpath:/META-INF/resources/webjars/");
	// }
	//
	// @Bean
	// public Docket api() {
	// return new
	// Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
	// .paths(PathSelectors.any()).build();
	// }

	@Bean
	public DataSource getDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(this.driverClassName);
		dataSource.setUrl(this.url);

		dataSource.setMaxIdle(this.maxIdle);
		dataSource.setMaxActive(this.maxActive);

		dataSource.setUsername(this.username);
		dataSource.setPassword(this.password);
		return dataSource;
	}

	@Bean
	public DataSourceTransactionManager transactionManager() {
		return new DataSourceTransactionManager(getDataSource());
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(getDataSource());
		return sessionFactory.getObject();
	}

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		return resolver;
	}

}
