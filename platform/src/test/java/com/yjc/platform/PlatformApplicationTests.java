package com.yjc.platform;

import com.alibaba.fastjson.JSON;
import com.yjc.platform.service.FriendService;
import com.yjc.platform.session.Session;
import com.yjc.platform.util.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;

@SpringBootTest
class PlatformApplicationTests {
	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Test
	public void test(){
		Long a = 1L;
		stringRedisTemplate.opsForValue().set(String.valueOf(a),"1");
	}

}
