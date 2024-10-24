package com.zimaberlin.zimasocial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class ZimaSocialApplication {
	public static void main(String[] args) {
		SpringApplication.run(ZimaSocialApplication.class, args);
	}
}
