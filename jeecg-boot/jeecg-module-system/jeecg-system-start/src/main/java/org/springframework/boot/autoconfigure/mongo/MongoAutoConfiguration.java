//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.boot.autoconfigure.mongo;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

@Primary
@AutoConfiguration
@ConditionalOnClass({MongoClient.class})
@ConditionalOnProperty(name = "spring.data.mongodb.uri", havingValue = "", matchIfMissing = false)
@EnableConfigurationProperties({MongoProperties.class})
@ConditionalOnMissingBean(
    type = {"org.springframework.data.mongodb.MongoDatabaseFactory"}
)
public class MongoAutoConfiguration {
    public MongoAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean({MongoClient.class})
    public MongoClient mongo(ObjectProvider<MongoClientSettingsBuilderCustomizer> builderCustomizers, MongoClientSettings settings) {
        return (MongoClient)(new MongoClientFactory((List)builderCustomizers.orderedStream().collect(Collectors.toList()))).createMongoClient(settings);
    }

    @Configuration(
        proxyBeanMethods = false
    )
    @ConditionalOnMissingBean({MongoClientSettings.class})
    static class MongoClientSettingsConfiguration {
        MongoClientSettingsConfiguration() {
        }

        @Bean
        MongoClientSettings mongoClientSettings() {
            return MongoClientSettings.builder().build();
        }

        @Bean
        MongoPropertiesClientSettingsBuilderCustomizer mongoPropertiesCustomizer(MongoProperties properties, Environment environment) {
            return new MongoPropertiesClientSettingsBuilderCustomizer(properties, environment);
        }
    }
}
