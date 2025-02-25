package uz.uat.backend.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Statik resurslar uchun handler
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/{path:[^\\.]*}")
                .setViewName("forward:/index.html");
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(true) // URL parametriga asoslangan content negotiation
                  .ignoreAcceptHeader(false) // Accept headerdan foydalanadi
                  .defaultContentType(MediaType.TEXT_HTML) // Default MIME turi
                  .mediaType("html", MediaType.TEXT_HTML) // .html uchun MIME turi
                  .mediaType("json", MediaType.APPLICATION_JSON); // .json uchun MIME turi
    }
}