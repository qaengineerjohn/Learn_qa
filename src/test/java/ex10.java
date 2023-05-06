import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ex10 {

    @ParameterizedTest
    @ValueSource (strings = {"Fox","Lazy dogs sleep more then cats"})
    public void testStringLength(String condition) {
        int length = condition.length();
        assertTrue(length > 15,"The text length < 15");
    }
}
