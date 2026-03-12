package com.cosmeticPlatform.CosmeticPlatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@EnableJpaRepositories(basePackages = "com.cosmeticPlatform.CosmeticPlatform.repository")
@EnableRedisRepositories(basePackages = "com.cosmeticPlatform.CosmeticPlatform.redisrepo") // Scans empty or different package
public class CosmeticPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(CosmeticPlatformApplication.class, args);
	}

}
