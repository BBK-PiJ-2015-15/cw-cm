import java.util.Calendar;
import java.util.Set;

/**
* A class to represent meetings
*
* Meetings have unique IDs, scheduled date and a list of participating contacts 
*/
public abstract class MeetingImpl implements Meeting {
    /**
     * The meeting id.
     */
    private int id;

    /**
     * The meeting data.
     */
    private Calendar date;

    /**
     * The set of meeting contacts.
     */
    private Set<Contact> contacts;

    /**
     * Creates a new meeting by passing the meeting id, date and a set of
     * contacts.
     *
     * @param id The meeting id.
     * @param date The meeting date.
     * @param contacts The set of contacts.
     * @throws IllegalArgumentException if the id is invalid (less than or equal
     *         to 0).
     * @throws NullPointerException If the date or the set of contacts are null.
     * @throws IllegalArgumentException If the set of contacts is empty.
     */
    public MeetingImpl(int id, Calendar date, Set<Contact> contacts) {
        if (id <= 0)
            throw new IllegalArgumentException("id must be greater than 0");
        else if (date == null || contacts == null)
            throw new NullPointerException("date or contacts must not be null");
        else if (contacts.isEmpty())
            throw new IllegalArgumentException("contacts must not be empty");

        this.id = id;
        this.date = date;
        this.contacts = contacts;
    }

    /**
     * Gets the meeting id.
     *
     * @return The meeting id.
     */
    public int getId() {
        return id;
    }
    
    /**
    * Gets the meeting date.
    *
    * @return The meeting date.
    */
    public Calendar getDate() {
        return date;
    }
    
    /**
     * Gets the details of the people attending the meeting.
     *
     * The set contains a minimum of one contact (if there were
     * just two people: the user and the contact) and may contain an 
     * arbitrary number of them.
     *
     * @return The set of meeting contacts.
     */
    public Set<Contact> getContacts() {
        return contacts;
    }
}
