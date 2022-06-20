package ieetu.common.utils;

import org.junit.jupiter.api.Test;

class PropertiesLoaderTest {
@Test
public void test() throws Exception {
    // given
    String s = PropertiesLoader.getProperties("spring.servlet.multipart.location").toString();
    System.out.println("s = " + s);

    // when


    // then

}
}