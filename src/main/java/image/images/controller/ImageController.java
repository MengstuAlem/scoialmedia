package image.images.controller;

import image.images.repository.ImageRepository;
import image.images.entity.Image;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;


@AllArgsConstructor
public class ImageController {
    private ImageRepository imageRepository;



    @GetMapping("/images")

    public Flux<Image> images(){
        return Flux.just(new Image("1","Designing a Reactive System"),
                new Image("2","Quick Start with Java"),
                new Image("3","Reactive Web with Spring Boot"));
    }

    @PostMapping("/images")

    public Mono<Void> createImage(@RequestBody Flux<Image> image){
        return image.map(
                new Function<Image, Image>() {
                    @Override
                    public Image apply(Image image) {
                        System.out.println("we will save" +image+"to reactive database");
                        return image;
                    }
                }
        ).then();
    }
}
