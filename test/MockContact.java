/**
 * A mock implementation of the contact interface.
 */
public class MockContact implements Contact {
  /**
   * Creates a new mock contact.
   */
  public MockContact() {
  }
  
  /**
   * Creates a new mock contact.
   *
   * All params are ignored.
   *
   * @param id The contact id.
   * @param name The contact name.
   */
  public MockContact(int id, String name) {
  }
  
  /**
   * Creates a new mock contact.
   *
   * All params are ignored.
   *
   * @param id The contact id.
   * @param name The contact name.
   * @param notes The contact notes.
   */
  public MockContact(int id, String name, String notes) {
  }
  
  /**
   * Gets the contact id; always returns 0;
   *
   * @return The contact id.
   */
  public int getId() {
    return 0;
  }
  
  /**
   * Gets the contact name; always returns "John Doe II".
   *
   * @return The contact name.
   */
  public String getName() {
    return "John Doe II";
  }
  
  /**
   * Get the contact notes; always returns the empty string.
   *
   *
   * @return The contact notes.
   */
  public String getNotes() {
    return "";
  }
  
  /**
   * Add notes about the contact; always does nothing.
   *
   * @param note The notes to add.
   */
  public void addNotes(String note) {
  }
}
