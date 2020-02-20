package image.images.service;

import image.images.entity.Image;
import image.images.repository.ImageRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
    public void findFileByName() throws IOException {
           when(loader.getResource(any())).thenReturn(new ByteArrayResource("data".getBytes()));


    }

}