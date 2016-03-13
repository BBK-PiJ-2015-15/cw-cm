import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 * A class to manage your contacts and meetings.
 */
public interface ContactManager {
  /**
   * Adds a new meeting to be held in the future.
   *
   * An ID is returned when the meeting is put into the system. This
   * ID must be positive and non-zero.
   *
   * @param contacts A list of contacts that will participate in the meeting.
   * @param date The date on which the meeting will take place.
   * @return The ID for the meeting
   * @throws IllegalArgumentException If the meeting is set for a time
   *         in the past, of if any contact is unknown / non-existent.
   * @throws NullPointerException If the meeting or the date are null.
   */
  int addFutureMeeting(Set<Contact> contacts, Calendar date);
  
  /**
   * Returns the PAST meeting with the requested ID, or null if it there is
   * none.
   *
   * The meeting must have happened at a past date.
   *
   * @param id The ID for the meeting.
   * @return The meeting with the requested ID, or null if it there is none.
   * @throws IllegalStateException If there is a meeting with that ID
   *         happening in the future.
   */
  PastMeeting getPastMeeting(int id);
  
  /**
   * Returns the FUTURE meeting with the requested ID, or null if there is none.
   *
   * @param id The ID for the meeting.
   * @return The meeting with the requested ID, or null if it there is none.
   * @throws IllegalArgumentException If there is a meeting with that ID
   *         happening in the past.
   */
  FutureMeeting getFutureMeeting(int id);
  
  /**
   * Returns the meeting with the requested ID, or null if it there is none.
   *
   * @param id The ID for the meeting.
   * @return The meeting with the requested ID, or null if it there is none.
   */
  Meeting getMeeting(int id);
  
  /**
   * Returns the list of future meetings scheduled with this contact.
   *
   * If there are none, the returned list will be empty. Otherwise,
   * the list will be chronologically sorted and will not contain any
   * duplicates.
   *
   * @param contact One of the users contacts.
   * @return The list of future meeting(s) scheduled with this contact
   *         (maybe empty).
   * @throws IllegalArgumentException If the contact does not exist.
   * @throws NullPointerException If the contact is null.
   */
  List<Meeting> getFutureMeetingList(Contact contact);
  
  /**
   * Returns the list of meetings that are scheduled for, or that took
   * place on, the specified date
   *
   * If there are none, the returned list will be empty. Otherwise,
   * the list will be chronologically sorted and will not contain any
   * duplicates.
   *
   * @param date The date.
   * @return The list of meetings.
   * @throws NullPointerException If the date is null.
   */
  List<Meeting> getMeetingListOn(Calendar date);
  
  /**
   * Returns the list of past meetings in which this contact has participated.
   *
   * If there are none, the returned list will be empty. Otherwise,
   * the list will be chronologically sorted and will not contain any
   * duplicates.
   *
   * @param contact One of the users contacts.
   * @return The list of future meeting(s) scheduled with this contact
   *         (maybe empty).
   * @throws IllegalArgumentException If the contact does not exist.
   * @throws NullPointerException If the contact is null.
   */
  List<PastMeeting> getPastMeetingListFor(Contact contact);
  
  /**
   * Creates a new record for a meeting that took place in the past.
   *
   * @param contacts A list of participants.
   * @param date The date on which the meeting took place.
   * @param text Messages to be added about the meeting.
   * @throws IllegalArgumentException If the list of contacts is
   *         empty, or any of the contacts does not exist.
   * @throws NullPointerException If any of the parameters are null.
   */
  void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text);
  
  /**
   * Adds notes to a meeting.
   *
   * This method is used when a future meeting takes place, and is
   * then converted to a past meeting (with notes) and returned.
   *
   * It can be also used to add notes to a past meeting at a later date.
   *
   * @param id The ID of the meeting.
   * @param text Messages to be added about the meeting.
   * @return The past meeting.
   * @throws IllegalArgumentException If the meeting does not exist.
   * @throws IllegalStateException If the meeting is set for a date in the
   *         future.
   * @throws NullPointerException If the notes are null.
   */
  PastMeeting addMeetingNotes(int id, String text);
  
  /**
   * Creates a new contact with the specified name and notes.
   *
   * @param name The name of the contact.
   * @param notes Notes to be added about the contact.
   * @return The ID for the new contact.
   * @throws IllegalArgumentException if the name or the notes are empty
   *         strings.
   * @throws NullPointerException If the name or the notes are null.
   */
  int addNewContact(String name, String notes);
  
  /**
   * Returns a list with the contacts whose name contains that string.
   *
   * If the string is the empty string, this methods returns the set
   * that contains all current contacts.
   *
   * @param name The string to search for.
   * @return A list with the contacts whose name contains that string.
   * @throws NullPointerException If the parameter is null.
   */
  Set<Contact> getContacts(String name);
  
  /**
   * Returns a list containing the contacts that correspond to the IDs.
   * Note that this method can be used to retrieve just one contact by passing
   * only one ID.
   *
   * @param ids An arbitrary number of contact IDs.
   * @return A list containing the contacts that correspond to the IDs.
   * @throws IllegalArgumentException If no IDs are provided or if
   *         any of the provided IDs does not correspond to a real contact.
   */
  Set<Contact> getContacts(int... ids);
  
  /**
   * Saves all data to disk.
   *
   * This method must be executed when the program is
   * closed and when/if the user requests it.
   */
  void flush();
}
