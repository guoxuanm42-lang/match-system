package com.yupi.yupao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.security.NoSuchAlgorithmException;

/**
 * 应用基础测试类（用于验证 Spring 容器与常用工具方法）。
 *
 * @author Ethan
 */
@SpringBootTest
class MyApplicationTest {

    @Test
    void testDigest() throws NoSuchAlgorithmException {
        String newPassword = DigestUtils.md5DigestAsHex(("abcd" + "mypassword").getBytes());
        System.out.println(newPassword);
    }

    @Test
    void contextLoads() {

    }

}
