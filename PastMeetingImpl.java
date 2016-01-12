import java.util.Calendar;
import java.util.Set;

/**
 * A meeting that was held in the past.
 *
 * It includes your notes about what happened and what was agreed.
 */
public class PastMeetingImpl extends MeetingImpl implements PastMeeting {
    /**
     * The past meeting notes.
     */
    private String notes;

    public PastMeetingImpl(int id, Calendar date, Set<Contact> contacts,
            String notes) {
        super(id, date, contacts);
        
        if (notes == null)
            throw new NullPointerException();
        
        this.notes = notes;
    }
    
    /**
     * Gets the past meeting notes.
     *
     * If there are no notes, an empty string is returned.
     *
     * @return The past meeting notes.
     */
    public String getNotes() {
        return notes;
    }
}
