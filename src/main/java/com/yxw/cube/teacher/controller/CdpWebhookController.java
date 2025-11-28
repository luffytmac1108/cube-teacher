package com.yxw.cube.teacher.controller;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;

@Slf4j
@RestController
public class CdpWebhookController {

    @PostMapping("/webhook")
    public String webhook(@RequestBody Object object, HttpServletRequest request) {

        System.out.println("\n\n--- 🚀 开始打印 HttpServletRequest 详细信息 ---");

        // 1. 打印基本请求信息
        System.out.println("## 基础信息");
        System.out.println("Method: " + request.getMethod());
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Context Path: " + request.getContextPath());
        System.out.println("Servlet Path: " + request.getServletPath());
        System.out.println("Query String: " + request.getQueryString());
        System.out.println("Remote Addr: " + request.getRemoteAddr());
        System.out.println("Local Port: " + request.getLocalPort());
        System.out.println("Scheme: " + request.getScheme());
        System.out.println("Protocol: " + request.getProtocol());
        System.out.println("Server Name: " + request.getServerName());
        System.out.println("Server Port: " + request.getServerPort());
        System.out.println("Character Encoding: " + request.getCharacterEncoding());
        System.out.println("Content Type: " + request.getContentType());

        // 2. 遍历并打印请求头 (Headers)
        System.out.println("\n\n## 🌐 请求头 (Headers)");
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames.hasMoreElements()) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                System.out.println("Header: " + headerName + " = " + headerValue);
            }
        } else {
            System.out.println("没有找到请求头。");
        }

        // 3. 遍历并打印 URL 参数 (Parameters)
        // 适用于 GET 请求的查询字符串和 POST 请求的 form-urlencoded 数据
        System.out.println("\n\n## ⚙️ URL/表单参数 (Parameters)");
        Enumeration<String> parameterNames = request.getParameterNames();
        if (parameterNames.hasMoreElements()) {
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                // 使用 getParameterValues 获取所有值，以防有同名参数
                String[] paramValues = request.getParameterValues(paramName);
                System.out.print("Parameter: " + paramName + " = [");
                if (paramValues != null) {
                    for (int i = 0; i < paramValues.length; i++) {
                        System.out.print(paramValues[i] + (i < paramValues.length - 1 ? ", " : ""));
                    }
                }
                System.out.println("]");
            }
        } else {
            System.out.println("没有找到请求参数。");
        }

        // 4. 遍历并打印请求属性 (Attributes)
        // 这些属性通常是在请求处理链中（如 Filter, Interceptor）设置的
        System.out.println("\n\n## ✨ 请求属性 (Attributes)");
        Enumeration<String> attributeNames = request.getAttributeNames();
        if (attributeNames.hasMoreElements()) {
            while (attributeNames.hasMoreElements()) {
                String attrName = attributeNames.nextElement();
                Object attrValue = request.getAttribute(attrName);
                System.out.println("Attribute: " + attrName + " = " +
                        (attrValue != null ? attrValue.getClass().getSimpleName() + ": " + attrValue.toString() : "null"));
            }
        } else {
            System.out.println("没有找到请求属性。");
        }


        String dataString = JSONUtil.toJsonStr(object);
        log.info("\n---- 收到webhook请求，数据为：{}", dataString);
        return dataString;
    }
}