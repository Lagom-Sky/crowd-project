package com.atguigu.crowd.test;

import com.atguigu.crowd.util.CrowdUtil;
import org.junit.Test;

public class CrowdTest {
    @Test
    public void testMd5(){
        String source = "123123";
        System.out.println(CrowdUtil.md5(source));
    }
}
