package com.project.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.project.utils.MenuHandleInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
//	@Bean
//	public MenuHandleInterceptor menuHandleInterceptor() {
//		return new MenuHandleInterceptor();
//	}

	@Autowired
	private MenuHandleInterceptor menuHandleInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(menuHandleInterceptor).addPathPatterns("/**");
	}

	@Bean
	public Module datatypeHibernateModule() {
		return new Hibernate5Module();
	}

}
