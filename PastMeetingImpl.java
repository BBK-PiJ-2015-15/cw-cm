import java.util.Calendar;
import java.util.Set;

/**
 * A meeting held in the past.
 *
 * It includes notes about what happened and what was agreed.
 */
public class PastMeetingImpl extends MeetingImpl implements PastMeeting {
    /**
     * The past meeting notes.
     */
    private String notes;

    /**
     * Creates a meeting held in the past from the meeting id, date, a set of
     * contacts and notes.
     *
     * @param id the meeting id.
     * @param date the meeting date.
     * @param contacts the set of contacts.
     * @param notes the meeting notes.
     * @throws IllegalArgumentException if the id is invalid (less than or equal
     *         to 0).
     * @throws NullPointerException if the date, the set of contacts or the
     *         notes are null.
     * @throws IllegalArgumentException if the set of contacts is empty.
     */
    public PastMeetingImpl(int id, Calendar date, Set<Contact> contacts,
            String notes) {
        super(id, date, contacts);
        
        if (notes == null)
            throw new NullPointerException();
        
        this.notes = notes;
    }
    
    /**
     * Returns the past meeting notes.
     *
     * If there are no notes, an empty string is returned.
     *
     * @return the past meeting notes.
     */
    public String getNotes() {
        return notes;
    }
    
    /**
     * Adds notes about the meeting.
     *
     * The note will be suffixed with an end of line character ('\n') if there's
     * notes already stored.
     *
     * @param notes the notes to add.
     */
    public void addNotes(String notes) {
        if (!this.notes.equals(""))
            this.notes += "\n";
        
        this.notes += notes;
    }
}
