package pl.lodz.p.it.ssbd2024.ssbd01.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.ExceptionHandlingController;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.OverrideSpringExceptionHandler;

import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "pl.lodz.p.it.ssbd2024.ssbd01.auth.controller",
        "pl.lodz.p.it.ssbd2024.ssbd01.mok.controller",
        "pl.lodz.p.it.ssbd2024.ssbd01.mok.converter"
})
@Import({ExceptionHandlingController.class, OverrideSpringExceptionHandler.class})
public class WebCoreConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
    }

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Override
    public Validator getValidator() {
        return WebMvcConfigurer.super.getValidator();
    }
}
