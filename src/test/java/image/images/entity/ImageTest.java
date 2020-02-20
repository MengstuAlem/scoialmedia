package image.images.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ImageTest {

    @Test

    public void createImageByLombokShouldWork(){
        Image image = new Image("1","introduction to java");
        assertThat(image.getId()).isEqualTo("1");
        assertThat(image.getName()).isEqualTo("introduction to java");
    }


}