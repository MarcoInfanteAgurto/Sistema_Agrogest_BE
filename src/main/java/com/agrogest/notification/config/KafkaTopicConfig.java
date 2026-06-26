package com.agrogest.notification.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic siembraCreadaTopic() {
        return TopicBuilder.name("siembra-creada")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic actividadCreadaTopic() {
        return TopicBuilder.name("actividad-creada")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic parcelaCreadaTopic() {
        return TopicBuilder.name("parcela-creada")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic usuarioCreadoTopic() {
        return TopicBuilder.name("usuario-creado")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic calendarioCreadoTopic() {
        return TopicBuilder.name("calendario-creado")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic cultivoCreadoTopic() {
        return TopicBuilder.name("cultivo-creado")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
