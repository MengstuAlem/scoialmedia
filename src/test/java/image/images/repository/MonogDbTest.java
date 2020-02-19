package image.images.repository;

import image.images.entity.Image;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class MonogDbTest {
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;
    private Image image;

    @BeforeEach
    void setUp() {
        image = new Image("any","mengstu");
    }

    @Test
    public void saveImageReturnsImage(){
        Mono<Image> saved =reactiveMongoTemplate.save(Mono.just(image)).then(reactiveMongoTemplate.findById("any",Image.class));
        StepVerifier.create(saved)
                .expectNextMatches(new Predicate<Image>() {
                    @Override
                    public boolean test(Image image) {
                        assertThat(image.getId()).isEqualTo("any");
                        assertThat(image.getName()).isEqualTo("mengstu");
                        return true;
                    }
                }).expectComplete()
                .verify();

    }


    @AfterEach

    public void deleteSaved(){
        reactiveMongoTemplate.remove(Mono.just(image));
    }

}

