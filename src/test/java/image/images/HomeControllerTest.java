package image.images;

import image.images.entity.Image;
import image.images.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@WebFluxTest
class HomeControllerTest {
    @MockBean
    private ImageService imageService;
    @Autowired
    private WebTestClient webTestClient;
    @InjectMocks
    private HomeController homeController;
    @MockBean
    private Resource resource;

    @BeforeEach
    public void setUp(){
        homeController=new HomeController(imageService);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void  shouldListAllImages(){
        when(imageService.findAllImages()).thenReturn(Flux.just(new Image("1","java"),new Image("2","kotlin")));

        EntityExchangeResult<String> result = webTestClient.get().uri("/")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .returnResult();

        assertThat(result.getResponseBody())
                .contains("href=\"/images/java/raw")
                .contains("href=\"/images/kotlin/raw\"");

        verify(imageService,times(1)).findAllImages();
    }


    @Test
    public void fetchingImageShouldWork(){
        when(imageService.findOneImage(any())).thenReturn(Mono.just(new ByteArrayResource("data".getBytes())));

        webTestClient.get().uri("/images/alpha.png/raw")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class).isEqualTo("data");
        verify(imageService).findOneImage("alpha.png");
        verifyNoMoreInteractions(imageService);
    }


    @Test
    public void fetchingNotExistImageThrowException() throws IOException {
        when(resource.getInputStream()).thenThrow(new IOException(" bad file"));
        when(imageService.findOneImage(any())).thenReturn(Mono.just(resource));

        webTestClient.get().uri("/images/alpha.png/raw")
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .isEqualTo("could findalpha.png bad file");
        verify(imageService,times(1)).findOneImage("alpha.png");
    }

    @Test
    public void deleteImageshouldWork(){
        when(imageService.deleteImage("alpha.png")).thenReturn(Mono.empty());

        webTestClient.delete().uri("/images/alpha.png/")
                .exchange()
                .expectStatus().isSeeOther()
                .expectHeader()
                .valueEquals(HttpHeaders.LOCATION,"/");
        verify(imageService,times(1)).deleteImage("alpha.png");
    }




}