import static org.junit.Assert.*;
import org.junit.*;

/**
 * Implementation of the ContactImpl test class.
 */
public class ContactImplTest {
    @Test(expected=IllegalArgumentException.class)
    public void constructingWithInvalidIdShouldThrow() {
        Contact contact = new ContactImpl(0, "name", "notes");
    }
    
    @Test(expected=NullPointerException.class)
    public void constructingWithNullNameShouldThrow() {
        Contact contact = new ContactImpl(1, null, "notes");
    
    }
    
    @Test(expected=NullPointerException.class)
    public void constructingWithNullNotesShouldThrow() {
        Contact contact = new ContactImpl(1, "name", null);
    }
}
