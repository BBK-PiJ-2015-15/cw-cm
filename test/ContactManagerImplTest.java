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
    public void testGettingFutureMeetingFromPastMeetingGetterShouldThrow() {
        // add contact
        contactManager.addNewContact("John Doe", "a note");
        
        // get all contacts
        Set<Contact> contacts = contactManager.getContacts("");
        
        // add future meeting
        int id = contactManager.addFutureMeeting(contacts, futureDate);
        
        contactManager.getPastMeeting(id);
    }
    
    @Test
    public void testGettingFutureMeetingList() {
        // add contacts
        final int[] ids = {
            contactManager.addNewContact("John Doe", "a note"),
            contactManager.addNewContact("Jane Doe", "another note"),
            contactManager.addNewContact("James Bond", "vodka martini")
        };
        
        // add future meeting with first and second contact one year in the
        // future
        Set<Contact> contacts = contactManager.getContacts(ids[0], ids[1]);
        Calendar date = Calendar.getInstance();
        date.add(Calendar.YEAR, 1);
        int firstMeetingId = contactManager.addFutureMeeting(contacts,
            date);
        
        // add a duplicate
        int secondMeetingId = contactManager.addFutureMeeting(contacts, date);
        assertNotEquals(firstMeetingId, secondMeetingId);
        
        // assert getting future meeting list doesn't return duplicates
        assertGetFutureMeetingListEquals(contactManager.getContact(ids[0]),
            contactManager.getFutureMeeting(firstMeetingId));
        
        // add future meeting with first and second contact
        int thirdMeetingId = contactManager.addFutureMeeting(contacts,
            futureDate);
        
        // assert getting future meeting list returns in chronologically order
        assertGetFutureMeetingListEquals(contactManager.getContact(ids[1]),
            contactManager.getFutureMeeting(thirdMeetingId),
            contactManager.getFutureMeeting(firstMeetingId));
        
        // assert getting future meeting list with a contact that will not
        // participate in any future meetings
        assertGetFutureMeetingListEquals(contactManager.getContact(ids[2]));
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
        
        // assert adding extra notes to meeting
        final String extraNotes = "extra notes";
        assertSame(pastMeeting, contactManager.addMeetingNotes(id, extraNotes));
        assertEquals(pastMeeting.getNotes(), notes + "\n" + extraNotes);
    }
    
    @Test(expected=NullPointerException.class)
    public void testAddingPastMeetingNotesWithNullNotesShouldThrow() {
        // add contact
        contactManager.addNewContact("John Doe", "a note");
        
        // get all contacts
        Set<Contact> contacts = contactManager.getContacts("");
        
        // add past meeting
        contactManager.addNewPastMeeting(contacts, pastDate, "meeting notes");
        int id = contactManager.getLastMeetingId();
        
        contactManager.addMeetingNotes(id, null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testGettingPastMeetingFromFutureMeetingGetterShouldThrow() {
        // add contact
        contactManager.addNewContact("John Doe", "a note");
        
        // get all contacts
        Set<Contact> contacts = contactManager.getContacts("");
        
        // add past meeting
        contactManager.addNewPastMeeting(contacts, pastDate, "meeting notes");
        
        contactManager.getFutureMeeting(contactManager.getLastMeetingId());
    }
    
    @Test(expected=NullPointerException.class)
    public void testGettingPastMeetingListWithNullContactShouldThrow() {
        contactManager.getPastMeetingListFor(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testGettingPastMeetingListWithUnknownContactShouldThrow() {
        Contact contact = mock(Contact.class);
        contactManager.getPastMeetingListFor(contact);
    }
    
    @Test
    public void testGettingPastMeetingList() {
        // add contacts
        final int[] ids = {
            contactManager.addNewContact("John Doe", "a note"),
            contactManager.addNewContact("Jane Doe", "another note"),
            contactManager.addNewContact("James Bond", "vodka martini")
        };
        final String notes = "meeting notes";
        
        // add past meeting with first and second contact
        Set<Contact> contacts = contactManager.getContacts(ids[0], ids[1]);
        contactManager.addNewPastMeeting(contacts, pastDate, notes);
        int firstMeetingId = contactManager.getLastMeetingId();
        
        // add a duplicate
        contactManager.addNewPastMeeting(contacts, pastDate, notes);
        int secondMeetingId = contactManager.getLastMeetingId();
        assertNotEquals(firstMeetingId, secondMeetingId);
        
        // assert getting past meeting list doesn't return duplicates
        assertGetPastMeetingListEquals(contactManager.getContact(ids[0]),
            contactManager.getPastMeeting(firstMeetingId));
        
        // add past meeting with first and second contact one year in the past
        Calendar date = Calendar.getInstance();
        date.add(Calendar.YEAR, -1);
        contactManager.addNewPastMeeting(contacts, date, notes);
        int thirdMeetingId = contactManager.getLastMeetingId();
        
        // assert getting past meeting list returns in chronologically order
        assertGetPastMeetingListEquals(contactManager.getContact(ids[1]),
            contactManager.getPastMeeting(thirdMeetingId),
            contactManager.getPastMeeting(firstMeetingId));
        
        // assert getting past meeting list with a contact that has not
        // participated in any past meetings
        assertGetPastMeetingListEquals(contactManager.getContact(ids[2]));
    }
    
    // add meeting notes tests
    
    @Test(expected=IllegalStateException.class)
    public void testAddingMeetingNotesToMeetingInFutureDateShouldThrow() {
        // add contacts
        contactManager.addNewContact("John Doe", "a note");
        contactManager.addNewContact("Jane Doe", "another note");
        contactManager.addNewContact("James Bond", "vodka martini");
        
        // get all contacts
        Set<Contact> contacts = contactManager.getContacts("");
        
        // add future meeting
        int id = contactManager.addFutureMeeting(contacts, futureDate);
        
        contactManager.addMeetingNotes(id, "meeting notes");
    }
    
    @Test
    public void testAddingMeetingNotes() {
        // add contacts
        contactManager.addNewContact("John Doe", "a note");
        contactManager.addNewContact("Jane Doe", "another note");
        contactManager.addNewContact("James Bond", "vodka martini");
        
        // get all contacts
        Set<Contact> contacts = contactManager.getContacts("");
        
        // create a date one second in the future
        Calendar date = Calendar.getInstance();
        futureDate.add(Calendar.SECOND, 1);
        
        // add future meeting
        int id = contactManager.addFutureMeeting(contacts, date);
        
        // get future meeting
        FutureMeeting futureMeeting = contactManager.getFutureMeeting(id);
        
        // wait until date is in future
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // assert adding notes converts meeting to a past meeting
        assertEquals(futureMeeting,
            contactManager.addMeetingNotes(id, "meeting notes"));
    }
    
    // contact tests
    
    @Test(expected=NullPointerException.class)
    public void testAddingContactWithNullNameShouldThrow() {
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
    public void testGettingContactsByIdWithoutIdsShouldThrow() {
        contactManager.getContacts();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testGettingContactsByIdWithNonExistingIdShouldThrow() {
        contactManager.getContacts(1);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testGettingContactByIdWithNonExistingIdShouldThrow() {
        contactManager.getContact(1);
    }
    
    @Test
    public void testAddingContact() {
        // add contact
        String name = "John Doe";
        int id = contactManager.addNewContact(name, "a note");
        assertTrue(id > 0);
        
        // assert getting all contacts contains contact
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
        
        // assert getting contacts by name contains contact
        assertEquals(contacts, contactManager.getContacts(name));
        
        // assert getting contacts by partial string in name contains contact
        assertEquals(contacts, contactManager.getContacts("Doe"));
        
        // assert gettting contacts by id contains contact
        assertEquals(contacts, contactManager.getContacts(id));
        
        // assert getting contact by id
        assertEquals(name, contactManager.getContact(id).getName());
    }
    
    // other tests
    
    @Test
    public void testGettingLastMeetingIdWithNoMeetingsShouldbeZero() {
        assertEquals(0, contactManager.getLastMeetingId());
    }
    
    @Test(expected=NullPointerException.class)
    public void testGettingMeetingListWithNullDateShouldThrow() {
        contactManager.getMeetingListOn(null);
    }
    
    @Test
    public void testGettingMeetingList() {
        final int[] ids = {
            contactManager.addNewContact("John Doe", "a note"),
            contactManager.addNewContact("Jane Doe", "another note"),
            contactManager.addNewContact("James Bond", "vodka martini"),
        };
        final String notes = "meeting notes";
        
        // add future meeting for first and second contacts
        Set<Contact> contacts = contactManager.getContacts(ids[0], ids[1]);
        int firstMeetingId = contactManager.addFutureMeeting(contacts,
            futureDate);
        
        // add duplicate
        contactManager.addFutureMeeting(contacts, futureDate);
        
        // assert getting meeting list doesn't return duplicates
        assertGetMeetingListEquals(futureDate, firstMeetingId);
        
        // add future meeting for first and third contacts
        contacts = contactManager.getContacts(ids[0], ids[2]);
        int secondMeetingId = contactManager.addFutureMeeting(contacts,
            futureDate);
        
        // assert getting meeting list returns sorted by id
        assertGetMeetingListEquals(futureDate, firstMeetingId, secondMeetingId);
        
        // add past meeting for first and second contacts
        contacts = contactManager.getContacts(ids[0], ids[1]);
        contactManager.addNewPastMeeting(contacts, pastDate, notes);
        int thirdMeetingId = contactManager.getLastMeetingId();
        
        // add duplicate
        contactManager.addNewPastMeeting(contacts, pastDate, notes);
        
        // assert getting meeting list doesn't return duplicates
        assertGetMeetingListEquals(pastDate, thirdMeetingId);
        
        // add past meeting for first and third contacts
        contacts = contactManager.getContacts(ids[0], ids[2]);
        contactManager.addNewPastMeeting(contacts, pastDate, notes);
        int fourthMeetingId = contactManager.getLastMeetingId();
        
        // assert getting meeting list returns sorted by id
        assertGetMeetingListEquals(pastDate, thirdMeetingId, fourthMeetingId);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAddingMeetingNotesWithUnknownIdShouldThrow() {
        contactManager.addMeetingNotes(1, "meeting notes");
    }
    
    // helper methods
    
    private void assertPastMeetingEquals(Meeting pastMeeting, Calendar date,
            Set<Contact> contacts, String notes) {
        assertTrue(pastMeeting instanceof PastMeeting);
        assertEquals(date, pastMeeting.getDate());
        assertEquals(contacts, pastMeeting.getContacts());
        assertEquals(notes, ((PastMeeting)pastMeeting).getNotes());
    }
    
    private void assertFutureMeetingEquals(Meeting futureMeeting, int id,
            Calendar date, Set<Contact> contacts) {
        assertTrue(futureMeeting instanceof FutureMeeting);
        assertEquals(id, futureMeeting.getId());
        assertEquals(date, futureMeeting.getDate());
        assertEquals(contacts, futureMeeting.getContacts());
    }
    
    private void assertFutureMeetingEquals(Meeting futureMeeting, Calendar date,
            Set<Contact> contacts) {
        assertTrue(futureMeeting instanceof FutureMeeting);
        assertTrue(futureMeeting.getId() > 0);
        assertEquals(date, futureMeeting.getDate());
        assertEquals(contacts, futureMeeting.getContacts());
    }
    
    private void assertGetFutureMeetingListEquals(Contact contact,
            FutureMeeting... expectedFutureMeetings) {
        List<Meeting> futureMeetings = contactManager.getFutureMeetingList(
            contact);
        assertEquals(expectedFutureMeetings.length, futureMeetings.size());
        
        for (int i = 0; i < expectedFutureMeetings.length; i++)
            assertEquals(futureMeetings.get(i), expectedFutureMeetings[i]);
    }
    
    private void assertGetPastMeetingListEquals(Contact contact,
            PastMeeting... expectedPastMeetings) {
        List<PastMeeting> pastMeetings = contactManager.getPastMeetingListFor(
            contact);
        assertEquals(expectedPastMeetings.length, pastMeetings.size());
        
        for (int i = 0; i < expectedPastMeetings.length; i++)
            assertEquals(pastMeetings.get(i), expectedPastMeetings[i]);
    }
    
    private void assertGetMeetingListEquals(Calendar date,
            int... expectedMeetingsIds) {
        List<Meeting> meetings = contactManager.getMeetingListOn(date);
        assertEquals(expectedMeetingsIds.length, meetings.size());
        
        for (int i = 0; i < expectedMeetingsIds.length; i++)
            assertEquals(meetings.get(i).getId(), expectedMeetingsIds[i]);
    }
}
