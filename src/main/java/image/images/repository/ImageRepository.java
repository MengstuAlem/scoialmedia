package image.images.repository;

import image.images.entity.Image;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ImageRepository extends ReactiveMongoRepository<Image, String> {
}
