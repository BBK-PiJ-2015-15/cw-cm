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
    private ContactManagerImpl contactManager;
    
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
        contactManager.addFutureMeeting(null, futureDate);
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
        contactManager.addFutureMeeting(new HashSet<Contact>(), futureDate);
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
        
        contactManager.addFutureMeeting(contacts, pastDate);
    }
    
    @Test
    public void testAddingFutureMeeting() {
        // add contacts
        contactManager.addNewContact("John Doe", "a note");
        contactManager.addNewContact("Jane Doe", "another note");
        contactManager.addNewContact("James Bond", "vodka martini");
        
        // get all contacts
        Set<Contact> contacts = contactManager.getContacts("");
        
        // add future meeting
        int id = contactManager.addFutureMeeting(contacts, futureDate);
        
        // assert last insert ID is the same
        assertEquals(id, contactManager.getLastMeetingId());
        
        // assert getting added future meeting returns correct meeting
        FutureMeeting futureMeeting = contactManager.getFutureMeeting(id);
        assertFutureMeetingEquals(futureMeeting, id, futureDate, contacts);
        
        // assert getting added future meeting using generic method returns
        // correct meeting
        assertEquals(futureMeeting, contactManager.getMeeting(id));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testGettingFutureMeetingFromPastMeetingMapShouldThrow() {
        // add contact
        contactManager.addNewContact("John Doe", "a note");
        
        // get all contacts
        Set<Contact> contacts = contactManager.getContacts("");
        
        // add future meeting
        int id = contactManager.addFutureMeeting(contacts, futureDate);
        
        contactManager.getPastMeeting(id);
    }
    
    // past meeting tests
    
    @Test(expected=NullPointerException.class)
    public void testAddingPastMeetingWithNullContactsShouldThrow() {
        contactManager.addNewPastMeeting(null, pastDate, "meeting notes");
    }
    
    @Test(expected=NullPointerException.class)
    public void testAddingPastMeetingWithNullDateShouldThrow() {
        // add a contact
        contactManager.addNewContact("John Doe", "a note");
        
        // get all contacts
        Set<Contact> contacts = contactManager.getContacts("");
        
        contactManager.addNewPastMeeting(contacts, null, "meeting notes");
    }
    
    @Test(expected=NullPointerException.class)
    public void testAddingPastMeetingWithNullNotesShouldThrow() {
        // add a contact
        contactManager.addNewContact("John Doe", "a note");
        
        // get all contacts
        Set<Contact> contacts = contactManager.getContacts("");
        
        contactManager.addNewPastMeeting(contacts, pastDate, null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAddingPastMeetingWithEmptyContactsShouldThrow() {
        contactManager.addNewPastMeeting(new HashSet<Contact>(), pastDate,
            "meeting notes");
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
    
    @Test
    public void testAddingPastMeeting() {
        // add contacts
        contactManager.addNewContact("John Doe", "a note");
        contactManager.addNewContact("Jane Doe", "another note");
        contactManager.addNewContact("James Bond", "vodka martini");
        
        // get all contacts
        Set<Contact> contacts = contactManager.getContacts("");
        String notes = "meeting notes";
        
        contactManager.addNewPastMeeting(contacts, pastDate, notes);
        
        // assert last insert ID is valid
        int id = contactManager.getLastMeetingId();
        assertTrue(id > 0);
        
        // assert getting added past meeting returns correct meeting
        PastMeeting pastMeeting = contactManager.getPastMeeting(id);
        assertPastMeetingEquals(pastMeeting, pastDate, contacts, notes);
        
        // assert getting added past meeting using generic method returns
        // correct meeting
        assertEquals(pastMeeting, contactManager.getMeeting(id));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testGettingPastMeetingFromFutureMeetingMapShouldThrow() {
        // add contact
        contactManager.addNewContact("John Doe", "a note");
        
        // get all contacts
        Set<Contact> contacts = contactManager.getContacts("");
        
        // add past meeting
        contactManager.addNewPastMeeting(contacts, pastDate, "meeting notes");
        
        contactManager.getFutureMeeting(contactManager.getLastMeetingId());
    }
    
    // contact tests
    
    @Test(expected=NullPointerException.class)
    public void testAddingContactWithNullNameShouldThrow() {
        // should throw
        contactManager.addNewContact(null, "a note");
    }
    
    @Test(expected=NullPointerException.class)
    public void testAddingContactWithNullNotesShouldThrow() {
        contactManager.addNewContact("John Doe", null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAddingContactWithEmptyNameShouldThrow() {
        contactManager.addNewContact("", "a note");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAddingContactWithEmptyNotesShouldThrow() {
        contactManager.addNewContact("John Doe", "");
    }
    
    @Test(expected=NullPointerException.class)
    public void testGettingContactsByNameWithNullNameShouldThrow() {
        String name = null;
        contactManager.getContacts(name);
    }
    
    @Test(expected=NullPointerException.class)
    public void testGettingContactsByIdWithNullIdsShouldThrow() {
        int[] ids = null;
        contactManager.getContacts(ids);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testGettingContactByIdWithoutIdsShouldThrow() {
        contactManager.getContacts();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testGettingContactByIdWithNonExistingIdShouldThrow() {
        contactManager.getContacts(1);
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
    
    // helper methods
    
    public void assertPastMeetingEquals(Meeting pastMeeting, Calendar date,
            Set<Contact> contacts, String notes) {
        assertTrue(pastMeeting instanceof PastMeeting);
        assertEquals(date, pastMeeting.getDate());
        assertEquals(contacts, pastMeeting.getContacts());
        assertEquals(notes, ((PastMeeting)pastMeeting).getNotes());
    }
    
    public void assertFutureMeetingEquals(Meeting futureMeeting, int id,
            Calendar date, Set<Contact> contacts) {
        assertTrue(futureMeeting instanceof FutureMeeting);
        assertEquals(id, futureMeeting.getId());
        assertEquals(date, futureMeeting.getDate());
        assertEquals(contacts, futureMeeting.getContacts());
    }
    
    public void assertFutureMeetingEquals(Meeting futureMeeting, Calendar date,
            Set<Contact> contacts) {
        assertTrue(futureMeeting instanceof FutureMeeting);
        assertTrue(futureMeeting.getId() > 0);
        assertEquals(date, futureMeeting.getDate());
        assertEquals(contacts, futureMeeting.getContacts());
    }
}
