package ieetu.common.config.etc;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EgovPropertiesTest {
    @Test
    public void getProperties() throws Exception {
        // given
        String filePath = EgovProperties.getProperty("Globals.file.path");

        // when


        // then
        Assertions.assertThat(filePath).isEqualTo("/Users/thkim/upload/");

    }
}