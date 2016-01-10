/**
 * The contact implementation class.
 *
 * A contact is a person we are making business with or may do in the future.
 *
 * Contacts have an ID (unique, a non-zero positive integer),
 * a name (not necessarily unique), and notes that the user
 * may want to save about them.
 */
public class ContactImpl implements Contact {
    /**
     * The contact id.
     */
    private int id;
    
    /**
     * The contact name.
     */
    private String name;
    
    /**
     * The contact notes.
     */
    private String notes;
    
    /**
     * Creates a new contact by passing the contact id, name and the notes.
     *
     * @param id The contact id.
     * @param name The contact name.
     * @param notes The contact notes.
     * @throws IllegalArgumentException if the id invalid (less than or equal
     *         to 0).
     * @throws NullPointerException If the name or notes are null.
     */
    public ContactImpl(int id, String name, String notes) {
        if (id <= 0)
            throw new IllegalArgumentException("id must be greater than 0");
        else if (name == null || notes == null) {
            throw new NullPointerException("name or notes must not be null");
        }
        
        this.id = id;
        this.name = name;
        this.notes = notes;
    }

    /**
     * Gets the contact id.
     *
     * @return The contact id.
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * Gets the contact name.
     *
     * @return The contact name.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Get the contact notes, if any.
     *
     * If we have not written anything about the contact, the empty
     * string is returned. 
     *
     * @return The contact notes, maybe empty.
     */
    public String getNotes() {
        return this.notes;
    }
    
    /**
     * Add notes about the contact. 
     *
     * @param note The notes to add.
     */
    public void addNotes(String note) {
        // not implemented yet
    }
}
