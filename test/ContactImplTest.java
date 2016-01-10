import static org.junit.Assert.*;
import org.junit.*;

/**
 * Implementation of the ContactImpl test class.
 */
public class ContactImplTest {
    @Test(expected=IllegalArgumentException.class)
    public void constructingWithInvalidIdShouldThrow3() {
        Contact contact = new ContactImpl(0, "name", "notes");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void constructingWithInvalidIdShouldThrow2() {
        Contact contact = new ContactImpl(0, "name");
    }
    
    @Test(expected=NullPointerException.class)
    public void constructingWithNullNameShouldThrow3() {
        Contact contact = new ContactImpl(1, null, "notes");
    
    }
    
    @Test(expected=NullPointerException.class)
    public void constructingWithNullNameShouldThrow2() {
        Contact contact = new ContactImpl(1, null);
    }
    
    @Test(expected=NullPointerException.class)
    public void constructingWithNullNotesShouldThrow3() {
        Contact contact = new ContactImpl(1, "name", null);
    }
    
    @Test
    public void retrievedIdNameAndOrNotesShouldBeTheSame() {
        Contact contact = new ContactImpl(1, "name", "notes");
        assertEquals(contact.getId(), 1);
        assertEquals(contact.getName(), "name");
        assertEquals(contact.getNotes(), "notes");
        
        contact = new ContactImpl(1, "name");
        assertEquals(contact.getId(), 1);
        assertEquals(contact.getName(), "name");
        assertEquals(contact.getNotes(), "");
    }
}
