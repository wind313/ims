package com.yjc.platform;

import com.alibaba.fastjson.JSON;
import com.yjc.platform.service.FriendService;
import com.yjc.platform.session.Session;
import com.yjc.platform.util.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class PlatformApplicationTests {
	@Autowired
	FriendService friendService;

	@Test
	public void test(){

	}

}
