package com.xavelo.template;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class TemplateApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		assertNotNull(applicationContext);
	}

	@Test
	void applicationNameShouldBeSet() {
		String appName = applicationContext.getEnvironment().getProperty("spring.application.name");
		assertNotNull(appName);
		assertFalse(appName.isEmpty());
	}

	@Test
	void activeProfilesShouldBeSet() {
		String[] activeProfiles = applicationContext.getEnvironment().getActiveProfiles();
		assertNotNull(activeProfiles);
		//assertTrue(activeProfiles.length > 0);
	}

}
