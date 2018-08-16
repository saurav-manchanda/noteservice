package com.bridgelabz.noteservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


/**
 * @author Saurav
 * @since 19/07/2018
 *        <p>
 *        This class for redis configuration. We want to get the beand for
 *        respective JedisConnection factory so as to get the connection with
 *        the redis repository. And also for getting the bean for respective
 *        Redis Template
 *        </p>
 */
@Configuration
@Component
public class RedisConfig {
	/**
	 * @return jedisConFactory
	 */
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
		return jedisConFactory;
	}

	/**
	 * @return redis template
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}
}
