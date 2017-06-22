package com.pard.common;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.pard.common.datatables.DataTableResolver;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.List;
import java.util.Properties;

/**
 * Created by wawe on 17/5/4.
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    @Value("${pard.fileUploadPath}")
    private String fileUploadPath = "/Users/wawe/upfile/";

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(dataTableArgumentResolver());
    }

    @Bean
    public HandlerMethodArgumentResolver dataTableArgumentResolver() {
        return new DataTableResolver();
    }

    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    private TemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        engine.setEnableSpringELCompiler(true);
        engine.addDialect(new LayoutDialect());
        engine.addDialect(springSecurityDialect());
        return engine;
    }

    private ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;
    }

    @Bean
    public DefaultKaptcha captchProducer() {
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Properties prop = new Properties();
        prop.setProperty("kaptcha.border", "no"); //图片边框
        prop.setProperty("kaptcha.border.color", "105,179,90"); //边框颜色
        prop.setProperty("kaptcha.noise.color", "black"); //干扰实现类
        prop.setProperty("kaptcha.image.width", "100");
        prop.setProperty("kaptcha.image.height", "35");
        prop.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");//图片样式
        prop.setProperty("kaptcha.textproducer.font.color", "black");//字体颜色
        prop.setProperty("kaptcha.textproducer.font.size", "30");//字体大小
        prop.setProperty("kaptcha.textproducer.char.length", "4");//验证码长度
        prop.setProperty("kaptcha.textproducer.char.space", "3");//文字间隔
        prop.setProperty("kaptcha.textproducer.char.string", "1234567890");//文本集合
//        prop.setProperty("kaptcha.textproducer.char.font.names", "宋体,楷体");
        Config config = new Config(prop);
        kaptcha.setConfig(config);
        return kaptcha;
    }

   /* @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"));
                container.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/402"));
                container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"));
                container.addErrorPages(new ErrorPage(Throwable.class, "/error/500"));
            }
        };
    }*/

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //加载文件上传目录
        registry.addResourceHandler("/upload/**").addResourceLocations("file://" + fileUploadPath);
        super.addResourceHandlers(registry);
    }
}
