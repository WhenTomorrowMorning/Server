package com.y2sg.wtm;

import com.y2sg.wtm.global.config.YamlPropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {"classpath:database/application-database.yml"}, factory = YamlPropertySourceFactory.class)
@PropertySource(value = {"classpath:oauth2/application-oauth2.yml"}, factory = YamlPropertySourceFactory.class)
@PropertySource(value = {"classpath:swagger/application-springdoc.yml"}, factory = YamlPropertySourceFactory.class)
public class WTMApplication {

    public static void main(String[] args) {
        SpringApplication.run(WTMApplication.class, args);
    }

}
