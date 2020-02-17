package image.images;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ImageTest {

    @Test

    public void createChapter(){
        Image image = new Image("1","introduction to java");
        assertThat(image.getId()).isEqualTo("1");
        assertThat(image.getName()).isEqualTo("introduction to java");
    }

}