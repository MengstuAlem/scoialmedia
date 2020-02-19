package image.images.repository;

import image.images.entity.Image;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class ImageRepositoryTest {

    @Autowired
   private ImageRepository imageRepository;
    private Image image;

    @BeforeEach
    void setUp() {
        image = new Image("any","mengstu");
    }

    @Test
    public void saveImageToDataBaseReturnImage(){
        Mono<Image> saved =  imageRepository.save(image).then(imageRepository.findByName("mengstu"));

        StepVerifier.create(saved)
               .expectNextMatches(image1 -> {
                   assertThat(image1.getId()).isEqualTo("any");
                   assertThat(image1.getName()).isEqualTo("mengstu");
                   return true;
               })
                .expectComplete()
                .verify();
    }

    @AfterEach
    public void deletedSavedImage(){
        imageRepository.delete(image);
    }

}