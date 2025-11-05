import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class DataProcessorTest {

    @Test
    fun `should process data correctly`() {
        val processor = DataProcessor()
        val input = "  hello world  "

        val result = processor.processData(input)

        assertEquals("HELLO WORLD", result)
    }
    
    @ParameterizedTest
    @CsvSource(
        "test, TEST",
        "Hello, HELLO", 
        "123, 123",
        "'', ''"
    )
    fun `should process various inputs correctly`(input: String, expected: String) {
        val processor = DataProcessor()

        val result = processor.processData(input)

        assertEquals(expected, result)
    }
}
