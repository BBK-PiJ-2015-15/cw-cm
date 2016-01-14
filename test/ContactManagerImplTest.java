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
     * The present date.
     */
    private Calendar presentDate;
    
    /**
     * A date in the future.
     */
    private Calendar futureDate;

    /**
     * A date in the past.
     */
    private Calendar pastDate;

    /**
     * The contact manager.
     */
    private ContactManager contactManager;
    
    public ContactManagerImplTest() {
        presentDate = Calendar.getInstance();
        
        futureDate = Calendar.getInstance();
        futureDate.add(Calendar.DATE, 1);
        
        pastDate = Calendar.getInstance();
        pastDate.add(Calendar.DATE, -1);
    }
    
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
        
        contactManager.addFutureMeeting(contacts, null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAddingFutureMeetingWithEmptyContactsShouldThrow() {
        contactManager.addFutureMeeting(new HashSet<Contact>(),
            futureDate);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAddingFutureMeetingWithUnknownContactShouldThrow() {
        // create a mock contact
        Contact contact = mock(Contact.class);
        
        // create set of contacts
        Set<Contact> contacts = new HashSet<Contact>();
        contacts.add(contact);
        
        contactManager.addFutureMeeting(contacts, futureDate);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAddingFutureMeetingWithPastDateShouldThrow() {
        // add a contact
        contactManager.addNewContact("John Doe", "a note");
        
        // get all contacts
        Set<Contact> contacts = contactManager.getContacts("");
        assertEquals(1, contacts.size());
        
        contactManager.addFutureMeeting(contacts, pastDate);
    }
    
    @Test
    public void testAddingFutureMeeting() {
        // add contacts
        contactManager.addNewContact("John Doe", "a note");
        contactManager.addNewContact("Jane Doe", "another note");
        contactManager.addNewContact("James Bond", "vodka martini");
        assertEquals(3, contactManager.getContacts("").size());
        
        // get all contacts with surname Doe
        Set<Contact> contacts = contactManager.getContacts("Doe");
        assertEquals(2, contacts.size());
        
        // add future meeting for contacts with surname Doe
        int id = contactManager.addFutureMeeting(contacts, futureDate);
        
        // get added future meeting
        FutureMeeting futureMeeting = contactManager.getFutureMeeting(id);
        assertNotNull(futureMeeting);
        assertEquals(futureMeeting.getId(), id);
        assertEquals(futureMeeting.getDate(), futureDate);
        assertTrue(contacts.containsAll(futureMeeting.getContacts()));
        
        // get added future meeting using generic method
        Meeting meeting = contactManager.getMeeting(id);
        assertNotNull(meeting);
        assertEquals(meeting.getId(), id);
        assertEquals(meeting.getDate(), futureDate);
        assertTrue(contacts.containsAll(meeting.getContacts()));
    }
    
    // past meeting tests
    
    @Test(expected=NullPointerException.class)
    public void testAddingPastMeetingWithNullContactsShouldThrow() {
        contactManager.addNewPastMeeting(null, pastDate, "meeting notes");
    }
    
    @Test(expected=NullPointerException.class)
    public void testAddingPastMeetingWithNullDateShouldThrow() {
        contactManager.addNewPastMeeting(new HashSet<Contact>(), null,
            "meeting notes");
    }
    
    @Test(expected=NullPointerException.class)
    public void testAddingPastMeetingWithNullNotesShouldThrow() {
        contactManager.addNewPastMeeting(new HashSet<Contact>(),
            pastDate, null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAddingPastMeetingWithEmptyContactsShouldThrow() {
        contactManager.addNewPastMeeting(new HashSet<Contact>(),
            pastDate, "meeting notes");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAddingPastMeetingWithUnknownContactShouldThrow() {
        // create a mock contact
        Contact contact = mock(Contact.class);
        
        // create set of contacts
        Set<Contact> contacts = new HashSet<Contact>();
        contacts.add(contact);
        
        contactManager.addNewPastMeeting(contacts, pastDate, "meeting notes");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAddingPastMeetingWithFutureDateShouldThrow() {
        // add a contact
        contactManager.addNewContact("John Doe", "a note");
        
        // get all contacts
        Set<Contact> contacts = contactManager.getContacts("");
        assertEquals(1, contacts.size());
        
        contactManager.addNewPastMeeting(contacts, futureDate, "meeting notes");
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
