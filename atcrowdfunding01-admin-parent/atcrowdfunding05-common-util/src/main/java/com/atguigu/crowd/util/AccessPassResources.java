package com.atguigu.crowd.util;

import java.util.HashSet;
import java.util.Set;

public class AccessPassResources {

    public static final Set<String> PASS_RES_SET = new HashSet<>();

    static {
        PASS_RES_SET.add("/");
        PASS_RES_SET.add("/auth/do/member/register");
        PASS_RES_SET.add("/auth/member/to/login");
        PASS_RES_SET.add("/auth/member/send/short/message.json");
        PASS_RES_SET.add("/auth/member/logout");
        PASS_RES_SET.add("/auth/member/to/login/page.html");
        PASS_RES_SET.add("/auth/member/to/reg/page.html");
//        PASS_RES_SET.add("/get/project/detail/*");
    }

    public static final Set<String> STATIC_RES_SET = new HashSet<>();

    static {
        STATIC_RES_SET.add("bootstrap");
        STATIC_RES_SET.add("css");
        STATIC_RES_SET.add("fonts");
        STATIC_RES_SET.add("img");
        STATIC_RES_SET.add("jquery");
        STATIC_RES_SET.add("layer");
        STATIC_RES_SET.add("ztree");
    }

    public static boolean judgeCurrentServletPathWhetherStaticResource(String servletPath) {

        if (servletPath == null || servletPath.length() == 0) {

            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATA);
        }
        String[] split = servletPath.split("/");

        String firstLevelPath = split[1];

        boolean contains = STATIC_RES_SET.contains(firstLevelPath);

        return contains;
    }
}
