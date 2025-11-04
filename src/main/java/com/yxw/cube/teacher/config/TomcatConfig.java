//package com.yxw.cube.teacher.config;
//
//import org.apache.catalina.connector.Connector;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
//import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class TomcatConfig {
//
//    // 443
//    @Value("${server.port}")
//    private int httpsPort;
//
//    @Bean
//    public ServletWebServerFactory servletContainer() {
//        // 创建内嵌的Tomcat工厂
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
//        // 添加一个 HTTP 连接器，用于监听 80 端口
//        tomcat.addAdditionalTomcatConnectors(createHttpConnector());
//        return tomcat;
//    }
//
//    private Connector createHttpConnector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        // 监听 80 端口
//        connector.setPort(80);
//        // 自动重定向到 HTTPS 端口
//        connector.setScheme("http");
//        connector.setSecure(false);
//        // 设置重定向目标地址为 443 端口
//        connector.setRedirectPort(httpsPort);
//        return connector;
//    }
//}