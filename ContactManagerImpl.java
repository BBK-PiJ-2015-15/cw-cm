import java.util.*;

/**
 * Class to manage contacts and meetings.
 */
public class ContactManagerImpl implements ContactManager {
    /**
     * The next contact ID.
     */
    private int nextContactId = 1;
    
    /**
     * The next meeting ID.
     */
    private int nextMeetingId = 1;

    /**
     * The set of contacts.
     */
    private HashSet<Contact> contacts = new HashSet<Contact>();
    
    /**
     * The set of future meetings.
     */
    private HashSet<Meeting> futureMeetings = new HashSet<Meeting>();
    
    /**
     * Constructs a new contact manager.
     */
    public ContactManagerImpl() {
    
    }
    
    /**
     * Adds a meeting to be held in the future.
     *
     * An ID is returned when the meeting is added to the system. This ID will
     * be positive and non-zero.
     *
     * @param contacts a set of contacts that will participate in the meeting
     * @param date the date on which the meeting will take place
     * @return the meeting ID
     * @throws IllegalArgumentException if the meeting is set for a time
     *         in the past or if any contact is unknown or non-existent
     * @throws NullPointerException if the contacts or date are null
     */
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        if (contacts == null || date == null) {
            throw new NullPointerException("contacts or date must not be null");
        } else if (!this.contacts.containsAll(contacts)) {
            throw new IllegalArgumentException(
                "contacts must not be unknown or non-existent");
        } else if (date.before(Calendar.getInstance())) {
            throw new IllegalArgumentException(
                "date must be set for a time in the future");
        }
        
        futureMeetings.add(new FutureMeetingImpl(nextMeetingId, date,
            contacts));
        
        return nextMeetingId++;
    }
    
    /**
     * Returns the past meeting with the specified ID, or null if there is none.
     *
     * The meeting must have happened at a past date.
     *
     * @param id the meeting ID
     * @return the meeting with the specified ID or null if there is none
     * @throws IllegalStateException if there is a meeting with that ID
     *         happening in the future
     */
    public PastMeeting getPastMeeting(int id) {
        return null;
    }
    
    /**
     * Returns the future meeting with the specified ID or null if there is
     * none.
     *
     * @param id the meeting ID
     * @return the meeting with the specified ID, or null if there is none
     * @throws IllegalArgumentException if there is a meeting with that ID
     *         happening in the past
     */
    public FutureMeeting getFutureMeeting(int id) {
        return null;
    }
    
    /**
     * Returns the meeting with the specified ID or null if there is none.
     *
     * @param id the meeting ID
     * @return the meeting with the requested ID or null if there is none
     */
    public Meeting getMeeting(int id) {
        return null;
    }
    
    /**
     * Returns a list of future meetings scheduled for this contact.
     *
     * If there are none, the returned list will be empty. Otherwise, it will be
     * chronologically sorted and will not contain any duplicates.
     *
     * @param contact the contact
     * @return a list of future meeting(s) scheduled for this contact (can be
     *         empty)
     * @throws IllegalArgumentException if the contact does not exist
     * @throws NullPointerException if the contact is null
     */
    public List<Meeting> getFutureMeetingList(Contact contact) {
        return null;
    }
    
    /**
     * Returns a list of meetings scheduled for or that took place on the
     * specified date.
     *
     * If there are none, the returned list will be empty. Otherwise, it will be
     * chronologically sorted and will not contain any duplicates.
     *
     * @param date the date
     * @return a list of meeting(s) schedule for or that took place on the
     *         specified date (can be empty)
     * @throws NullPointerException if the date is null
     */
    public List<Meeting> getMeetingListOn(Calendar date) {
        return null;
    }
    
    /**
     * Returns a list of past meetings in which this contact participated.
     *
     * If there are none, the returned list will be empty. Otherwise, it will be
     * chronologically sorted and will not contain any duplicates.
     *
     * @param contact the contact
     * @return a list of past meeting(s) in which the contact participated (can
     *         be empty)
     * @throws IllegalArgumentException if the contact does not exist
     * @throws NullPointerException if the contact is null
     */
    public List<PastMeeting> getPastMeetingListFor(Contact contact) {
        return null;
    }
    
    /**
     * Adds a new record for a meeting held in the past.
     *
     * @param contacts a set of participants
     * @param date the date on which the meeting took place
     * @param notes notes to be added about the meeting
     * @throws IllegalArgumentException if the list of contacts is empty or any
     *         of the contacts does not exist
     * @throws NullPointerException if any of the arguments are null
     */
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date,
            String notes) {
        if (contacts == null || date == null || notes == null) {
            throw new NullPointerException(
                "contacts, date or notes must not be null");
        }
    }
    
    /**
     * Adds notes to a meeting.
     *
     * This method is used when a future meeting takes place and is
     * then converted to a past meeting (with notes) and returned.
     *
     * It can be also used to add notes to a past meeting at a later date. 
     *
     * @param id the meeting ID
     * @param notes meeting notes to be added
     * @throws IllegalArgumentException if the meeting does not exist
     * @throws IllegalStateException if the meeting is set for a date in the
     *         future
     * @throws NullPointerException if the notes is null
     */
    public PastMeeting addMeetingNotes(int id, String notes) {
        return null;
    }
    
    /**
     * Adds a new contact with the specified name and notes.
     *
     * @param name contact name
     * @param notes contact notes to be added
     * @return the contact id
     * @throws IllegalArgumentException if the name or the notes are empty
     *         strings
     * @throws NullPointerException if the name or notes are null
     */
    public int addNewContact(String name, String notes) {
        if (name.isEmpty() || notes.isEmpty()) {
            throw new IllegalArgumentException(
                "name or notes must not be empty");
        }
        
        contacts.add(new ContactImpl(nextContactId, name, notes));
        
        return nextContactId++;
    }
    
    /**
     * Returns a set with the contacts whose name contains a specified string.
     *
     * If the specified string is empty, this methods returns the set that
     * contains all contacts.
     *
     * @param name the search string
     * @return a list with the contacts whose name contains the specified string
     * @throws NullPointerException if the name is null
     */
    public Set<Contact> getContacts(String name) {
        if (name == null)
            throw new NullPointerException("name must not be null");
    
        Set<Contact> newContacts = new HashSet<Contact>();
        if (name.isEmpty()) {
            newContacts.addAll(contacts);
        } else {
            name = name.toLowerCase();
            
            for (Contact contact : contacts) {
                if (contact.getName().toLowerCase().contains(name))
                    newContacts.add(contact);
            }
        }
        return newContacts;
    }
    
    /**
    * Returns a list containing the contacts that correspond to the IDs.
    *
    * Note that this method can be used to retrieve just one contact by passing
    * only one ID.
    *
    * @param ids an arbitrary number of contact IDs
    * @return a set containing the contacts that correspond to the IDs
    * @throws IllegalArgumentException if no IDs are provided or if any of the
    *         provided IDs do not correspond to a real contact
    */
    public Set<Contact> getContacts(int... ids) {
        if (ids == null)
            throw new NullPointerException("ids must not be null");
        
        Set<Contact> newContacts = new HashSet<Contact>();
        
        for (int id : ids) {
            for (Contact contact : contacts) {
                if (contact.getId() == id)
                    newContacts.add(contact);
            }
        }
        
        if (newContacts.isEmpty()) {
            throw new IllegalArgumentException(
                "no id provided or non existing contact");
        }
        return contacts;
    }
    
    /**
     * Saves all data to disk.
     *
     * This method must be executed when the program is
     * closed and when/if the user requests it. 
     */
    public void flush() {
    
    }
}
