package image.images.repository;

import image.images.entity.Image;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Mono;

public interface ImageRepository extends ReactiveMongoRepository<Image, String>{
    Mono<Image> findByName(String name);
}
