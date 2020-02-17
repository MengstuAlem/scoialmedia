package image.images;

import org.reactivestreams.Publisher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner init(ImageRepository chapterRepository){
        return args -> {
            chapterRepository.deleteAll().thenMany(
                   Flux.just(new Image("1","Designing a Reactive System"),
                              new Image("2","Quick Start with Java"),
                            new Image("3","Reactive Web with Spring Boot"))
                           .flatMap((Function<Image, Publisher<Image>>) image -> chapterRepository.save(image))).thenMany(chapterRepository.findAll())
            .subscribe(image -> System.out.println(image));


        };
    }
}
