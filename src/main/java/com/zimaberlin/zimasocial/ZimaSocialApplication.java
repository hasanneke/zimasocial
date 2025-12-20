package com.zimaberlin.zimasocial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication
@EnableAspectJAutoProxy
public class ZimaSocialApplication {
	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Europe/Istanbul")));
		SpringApplication.run(ZimaSocialApplication.class, args);
	}

	@RestController
	@RequestMapping(path = "/hello")
	class LoadTester {
		@GetMapping
		String hello() {
			return "hello";
		}
	}
}
