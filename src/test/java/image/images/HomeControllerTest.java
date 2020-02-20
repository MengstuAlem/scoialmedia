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
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

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

        assertThat(result.getResponseBody()).contains("href=\"/images/java/raw").contains("href=\"/images/kotlin/raw\"");

        verify(imageService,times(1)).findAllImages();
    }




}