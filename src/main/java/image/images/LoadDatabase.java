package image.images;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner init(ChapterRepository chapterRepository){
        return args -> {
            chapterRepository.deleteAll().thenMany(
                   Flux.just(new Chapter("1","Designing a Reactive System"),
                              new Chapter("2","Quick Start with Java"),
                            new Chapter("3","Reactive Web with Spring Boot")).map(chapter -> chapterRepository.save(chapter)));
            chapterRepository.findAll().subscribe(chapter -> System.out.println(chapter));
        };
    }
}
