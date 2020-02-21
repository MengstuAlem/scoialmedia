package image.images.service;

import image.images.entity.Image;
import image.images.repository.ImageRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataMongoTest
class ImageServiceTest {
    @MockBean
    private ImageRepository imageRepository;
    @MockBean
    private ResourceLoader loader;
    @InjectMocks
    private ImageService imageService;
    @MockBean
    private Resource resource;


    @BeforeEach
    public void setUp(){
        imageService= new ImageService(loader,imageRepository);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllImages() {
        when(imageRepository.findAll()).thenReturn(Flux.just(new Image("1", "java"), new Image("2", "kotlin")));
        StepVerifier.create(imageService.findAllImages())
                .recordWith(ArrayList::new)
                .expectNextCount(2)
                .consumeRecordedWith(images -> {
                    assertThat(images.size()).isEqualTo(2);
                    assertThat(images)
                            .extracting( image -> image.getName()).contains("java","kotlin");
                }).expectComplete()
                .verify();
    }


    @Test
    @Disabled
    public void findFileByName() throws IOException {

           when(resource.getFilename()).thenReturn("file.jpg");
        loader.getResource("file.jpg").getFilename();

           StepVerifier.create(imageService.findOneImage("file.jpg"))
                   .expectNextMatches(new Predicate<Resource>() {
                       @Override
                       public boolean test(Resource resource) {
                           assertThat(resource.getFilename()).isEqualTo("file.jpg");
                           return true;
                       }
                   }).expectComplete().verify();
    }



}