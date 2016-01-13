import java.util.*;
import static org.junit.Assert.*;
import org.junit.*;
import static org.mockito.Mockito.*;

/**
 * ContactManagerImpl unit test.
 *
 * Test cases:
 *   -
 */
public class ContactManagerImplTest {
    /**
     * The contact manager.
     */
    private ContactManager contactManager;
    
    @Before
    public void setUp() {
        contactManager = new ContactManagerImpl();
    }
    
    @Test(expected=NullPointerException.class)
    public void testAddingFutureMeetingWithNullContactsShouldThrow() {
        // should throw
        contactManager.addFutureMeeting(null, Calendar.getInstance());
    }
    
    @Test(expected=NullPointerException.class)
    public void testAddingFutureMeetingWithNullDateShouldThrow() {
        // should throw
        contactManager.addFutureMeeting(new HashSet<Contact>(), null);
    }
}
