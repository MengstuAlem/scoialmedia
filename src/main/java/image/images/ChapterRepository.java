package image.images;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ChapterRepository extends ReactiveMongoRepository<Chapter, String> {
}
