import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

class UserValidationTest {

    @Test
    fun `should validate correct user data`() {
        val validator = UserValidator()
        val user = User("john@example.com", "John", 25)
        
        val result = validator.validate(user)
        
        assertTrue(result.isValid)
    }
    
    @Test
    fun `should throw exception when email is empty`() {
        val validator = UserValidator()
        val user = User("", "John", 25)
        
        assertThrows<ValidationException> {
            validator.validate(user)
        }
    }
    
    @Test
    fun `should reject user with negative age`() {
        val validator = UserValidator()
        val user = User("john@example.com", "John", -5)
        
        val result = validator.validate(user)
        
        assertFalse(result.isValid)
        assertEquals("Age cannot be negative", result.errorMessage)
    }
}
