/**
 * A contact is a person we are making business with or may do in the future.
 *
 * Contacts have an ID (unique, a non-zero positive integer),
 * a name (not necessarily unique), and notes that the user
 * may want to save about them.
 */
public interface Contact {
  /**
   * Returns the id of the contact.
   *
   * @return The contact id.
   */
  int getId();
  
  /**
   * Returns the name of the contact.
   *
   * @return The contact name.
   */
  String getName();
  
  /**
   * Returns our notes about the contact, if any.
   *
   * If we have not written anything about the contact, the empty
   * string is returned.
   *
   * @return A string with notes about the contact, maybe empty.
   */
  String getNotes();
  
  /**
   * Adds notes about the contact.
   *
   * @param note The notes to be added
   */
  void addNotes(String note);
}
