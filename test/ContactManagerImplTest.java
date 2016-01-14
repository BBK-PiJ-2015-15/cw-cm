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
    
    // future meeting tests
    
    @Test(expected=NullPointerException.class)
    public void testAddingFutureMeetingWithNullContactsShouldThrow() {
        contactManager.addFutureMeeting(null, Calendar.getInstance());
    }
    
    @Test(expected=NullPointerException.class)
    public void testAddingFutureMeetingWithNullDateShouldThrow() {
        // add a contact
        contactManager.addNewContact("John Doe", "a note");
        
        // get all contacts
        Set<Contact> contacts = contactManager.getContacts("");
        
        // should throw
        contactManager.addFutureMeeting(contacts, null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAddingFutureMeetingWithPastDateShouldThrow() {
        // add a contact
        contactManager.addNewContact("John Doe", "a note");
        
        // get all contacts
        Set<Contact> contacts = contactManager.getContacts("");
        
        // create a date one day in the past
        Calendar pastDate = Calendar.getInstance();
        pastDate.add(Calendar.DATE, -1);
        
        // should throw
        contactManager.addFutureMeeting(contacts, pastDate);
    }
    
    // past meeting tests
    
    @Test(expected=NullPointerException.class)
    public void testAddingPastMeetingWithNullContactsShouldThrow() {
        // should throw
        contactManager.addNewPastMeeting(null, Calendar.getInstance(), "");
    }
    
    @Test(expected=NullPointerException.class)
    public void testAddingPastMeetingWithNullDateShouldThrow() {
        // should throw
        contactManager.addNewPastMeeting(new HashSet<Contact>(), null, "");
    }
    
    @Test(expected=NullPointerException.class)
    public void testAddingPastMeetingWithNullNotesShouldThrow() {
        // should throw
        contactManager.addNewPastMeeting(new HashSet<Contact>(),
            Calendar.getInstance(), null);
    }
    
    // contact tests
    
    @Test(expected=NullPointerException.class)
    public void testAddingContactWithNullNameShouldThrow() {
        // should throw
        contactManager.addNewContact(null, "a note");
    }
    
    @Test(expected=NullPointerException.class)
    public void testAddingContactWithNullNotesShouldThrow() {
        // should throw
        contactManager.addNewContact("John Doe", null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAddingContactWithEmptyNameShouldThrow() {
        // should throw
        contactManager.addNewContact("", "a note");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAddingContactWithEmptyNotesShouldThrow() {
        // should throw
        contactManager.addNewContact("John Doe", "");
    }
    
    @Test(expected=NullPointerException.class)
    public void testGettingContactsWithNullNameShouldThrow() {
        String name = null;
    
        // should throw
        contactManager.getContacts(name);
    }
    
    @Test(expected=NullPointerException.class)
    public void testGettingContactsWithNullIdsShouldThrow() {
        int[] ids = null;
        
        // should throw
        contactManager.getContacts(ids);
    }
    
    @Test
    public void testAddingContact() {
        // add contact
        String name = "John Doe";
        int id = contactManager.addNewContact(name, "a note");
        assertTrue(id > 0);
        
        // contact must be in the set containing all contacts
        Set<Contact> contacts = contactManager.getContacts("");
        assertEquals(1, contacts.size());
        
        boolean found = false;
        for (Contact contact : contacts) {
            if (contact.getName().equals(name)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
        
        // get contact by name
        assertEquals(contacts, contactManager.getContacts(name));
        
        // get contact by partial string in name
        assertEquals(contacts, contactManager.getContacts("Doe"));
        
        // get contact by id
        assertEquals(contacts, contactManager.getContacts(id));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testGettingContactByIdWithoutIdsShouldThrow() {
        contactManager.getContacts();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testGettingContactByIdWithNonExistingIdShouldThrow() {
        contactManager.getContacts(1);
    }
}
