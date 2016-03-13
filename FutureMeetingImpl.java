import java.util.Calendar;
import java.util.Set;

/**
 * A meeting to be held in the future.
 */
public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {
  /**
   * Creates a meeting to be held in the future from the meeting id, date and
   * a set of contacts.
   *
   * @param id The meeting id.
   * @param date The meeting date.
   * @param contacts The set of contacts.
   * @throws IllegalArgumentException If the id is invalid
   *         (less than or equal to 0).
   * @throws NullPointerException If the date or the set of contacts are null.
   * @throws IllegalArgumentException If the set of contacts is empty.
   */
  public FutureMeetingImpl(int id, Calendar date, Set<Contact> contacts) {
    super(id, date, contacts);
  }
}
