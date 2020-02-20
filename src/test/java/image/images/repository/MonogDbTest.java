package image.images.repository;

import com.mongodb.client.result.DeleteResult;
import image.images.entity.Image;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;


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
    public void deleteSavedImage(){
        reactiveMongoTemplate.remove(Mono.just(image)).subscribe();
    }







}

