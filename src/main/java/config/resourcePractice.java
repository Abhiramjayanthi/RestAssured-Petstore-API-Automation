package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class resourcePractice {
    public static void main(String[] args) {
        try (InputStream input=resourcePractice.class.getClassLoader().getResourceAsStream("config.properties")){
            System.out.println(input);
            if(input==null){
                throw new IllegalStateException("config.properties file not found");
            }
            Properties properties = new Properties();
            properties.load(input);
            String url = properties.getProperty("base.url");
            String qaUrl = properties.getProperty("qa.base.url");
            String stgUrl = properties.getProperty("stg.base.url");
            String devUrl = properties.getProperty("dev.base.url");
            System.out.println(url);
            System.out.println(qaUrl);
            System.out.println(devUrl);
            System.out.println(stgUrl);
        } catch (IOException e){
            throw new RuntimeException("Failed to load configuration", e);
        }
    }
}
