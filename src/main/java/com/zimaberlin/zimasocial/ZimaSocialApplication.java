package com.zimaberlin.zimasocial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication
@EnableAspectJAutoProxy
public class ZimaSocialApplication {
	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Europe/Istanbul")));
		SpringApplication.run(ZimaSocialApplication.class, args);
	}
}
