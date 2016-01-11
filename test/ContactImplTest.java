import java.util.*;
import static org.junit.Assert.*;
import org.junit.*;

/**
 * Implementation of the ContactImpl test class.
 */
public class ContactImplTest {
    private static Random random;

    private final String name = "John Doe";
    private final String notes = "A note about John Doe";
    private int id;
    
    /**
     * Generates the random number seed.
     */
    @BeforeClass
    public static void seed() {
        random = new Random();
    }
    
    /**
     * Generates a random id in the range 1 to Integer.MAX_VALUE.
     */
    @Before
    public void generateRandomId() {
        id = random.nextInt(Integer.MAX_VALUE - 1) + 1;
    }
    
    @Test
    public void testNewContactWithNegativeIdShouldThrow() {
        // test constructor with three arguments
        assertThrowsException(IllegalArgumentException.class, () -> {
            Contact contact = new ContactImpl(-1, name, notes);
        });

        // test constructor with two arguments
        assertThrowsException(IllegalArgumentException.class, () -> {
            Contact contact = new ContactImpl(-1, name);
        });
    }
    
    @Test
    public void testNewContactWithZeroIdShouldThrow() {
        // test constructor with three arguments
        assertThrowsException(IllegalArgumentException.class, () -> {
            Contact contact = new ContactImpl(0, name, notes);
        });

        // test constructor with two arguments
        assertThrowsException(IllegalArgumentException.class, () -> {
            Contact contact = new ContactImpl(0, name);
        });
    }
    
    @Test
    public void testNewContactWithNullNameShouldThrow() {
        // test constructor with three arguments
        assertThrowsException(NullPointerException.class, () -> {
            Contact contact = new ContactImpl(id, null, notes);
        });
        
        // test constructor with two arguments
        assertThrowsException(NullPointerException.class, () -> {
            Contact contact = new ContactImpl(id, null);
        });
    }
    
    @Test(expected=NullPointerException.class)
    public void testNewContactWithNullNotesShouldThrow() {
        Contact contact = new ContactImpl(id, name, null);
    }
    
    @Test
    public void testNewContactWithNameAndNotes() {
        // test constructor with three arguments
        Contact contact = new ContactImpl(id, name, notes);
        assertEquals(contact.getId(), id);
        assertEquals(contact.getName(), name);
        assertEquals(contact.getNotes(), notes);
        
        // test constructor with two arguments
        contact = new ContactImpl(id, name);
        assertEquals(contact.getId(), id);
        assertEquals(contact.getName(), name);
        assertEquals(contact.getNotes(), "");
    }
    
    @Test
    public void testNewContactWithAddedNotes() {
        final String extraNotes[] = { "new note about contact", "end note" };
    
        // test constructor with three arguments
        Contact contact = new ContactImpl(id, name, notes);
        
        contact.addNotes(extraNotes[0]);
        assertEquals(contact.getNotes(), notes + "\n" + extraNotes[0]);
        
        contact.addNotes(extraNotes[1]);
        assertEquals(contact.getNotes(), notes + "\n" + extraNotes[0] + "\n" +
            extraNotes[1]);
        
        // test constructor with two arguments
        contact = new ContactImpl(id, name);
        
        contact.addNotes(extraNotes[0]);
        assertEquals(contact.getNotes(), extraNotes[0]);
        
        contact.addNotes(extraNotes[1]);
        assertEquals(contact.getNotes(), extraNotes[0] + "\n" + extraNotes[1]);
    }
    
    /**
     * Helper assertion method to assert that a runnable code throws a
     * particular exception.
     *
     * This method allows a single JUnit test method to assert for multiple
     * exceptions.
     */
    private void assertThrowsException(
            Class<? extends Exception> expectedExceptionClass,
            Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            if (expectedExceptionClass.isInstance(e))
                return;
        }
        fail();
    }
}
