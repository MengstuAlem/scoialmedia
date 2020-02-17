package image.images;

import image.images.entity.Image;
import image.images.repository.ImageRepository;
import org.reactivestreams.Publisher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;


public class LoadDatabase {


    CommandLineRunner init(ImageRepository imageRepository){
        return args -> {
            imageRepository.deleteAll().thenMany(
                   Flux.just(new Image("1","Designing a Reactive System"),
                              new Image("2","Quick Start with Java"),
                            new Image("3","Reactive Web with Spring Boot"))
                           .flatMap((Function<Image, Publisher<Image>>) image -> imageRepository.save(image))).thenMany(imageRepository.findAll())
            .subscribe(image -> System.out.println(image));


        };
    }
}
