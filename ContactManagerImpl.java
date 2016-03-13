import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.StringBuilder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

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
  private TreeMap<Integer, Contact> contacts = new TreeMap<>();
  
  /**
   * The map of future meetings.
   */
  private TreeMap<Integer, FutureMeetingImpl> futureMeetings = new TreeMap<>();
  
  /**
   * The map of past meetings.
   */
  private TreeMap<Integer, PastMeetingImpl> pastMeetings = new TreeMap<>();
  
  /**
   * The database file.
   */
  private File file = new File("contacts.txt");
  
  /**
   * Constructs a new contact manager.
   */
  public ContactManagerImpl() {
    load();
  }
  
  /**
   * Returns the ID of the last added meeting.
   *
   * @return The last added meeting ID or 0 if there's no meetings.
   */
  public int getLastMeetingId() {
    return (nextMeetingId - 1);
  }
  
  /**
   * Adds a meeting to be held in the future.
   *
   * An ID is returned when the meeting is added to the system. This ID will
   * be positive and non-zero.
   *
   * @param contacts A set of contacts that will participate in the meeting.
   * @param date The date on which the meeting will take place.
   * @return The meeting ID.
   * @throws IllegalArgumentException If the meeting is set for a time
   *         in the past or if any contact is unknown.
   * @throws NullPointerException If the contacts or date are null.
   * @see Contact
   * @see FutureMeeting
   */
  public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
    if (contacts == null || date == null) {
      throw new NullPointerException("contacts or date must not be null");
    } else if (!this.contacts.values().containsAll(contacts)) {
      throw new IllegalArgumentException("contacts must not be unknown");
    } else if (date.compareTo(Calendar.getInstance()) < 0) {
      throw new IllegalArgumentException(
        "date must be set for a time in the future");
    }
    
    // create future meeting
    FutureMeetingImpl futureMeeting = new FutureMeetingImpl(nextMeetingId,
      date, contacts);
    
    // add meeting to map
    futureMeetings.put(nextMeetingId, futureMeeting);
    
    // increment next meeting ID
    return nextMeetingId++;
  }
  
  /**
   * Returns the past meeting with the specified ID, or null if there is none.
   *
   * The meeting must have happened at a past date.
   *
   * @param id The meeting ID.
   * @return The meeting with the specified ID or null if there is none.
   * @throws IllegalStateException If there is a meeting with that ID
   *         happening in the future.
   * @see PastMeeting
   */
  public PastMeeting getPastMeeting(int id) {
    PastMeeting pastMeeting = pastMeetings.get(id);
    
    // if not found, make sure it's not in the past meeting map
    if (pastMeeting == null && futureMeetings.get(id) != null) {
      throw new IllegalArgumentException(
        "meeting id must not be of meeting to be held in the future");
    }
    return pastMeeting;
  }
  
  /**
   * Returns the future meeting with the specified ID or null if there is
   * none.
   *
   * @param id The meeting ID.
   * @return The meeting with the specified ID or null if there is none.
   * @throws IllegalArgumentException If there is a meeting with that ID
   *         happening in the past.
   * @see FutureMeeting
   */
  public FutureMeeting getFutureMeeting(int id) {
    FutureMeeting futureMeeting = futureMeetings.get(id);
    
    // if not found, make sure it's not in the past meeting map
    if (futureMeeting == null && pastMeetings.get(id) != null) {
      throw new IllegalArgumentException(
        "meeting id must not be of meeting held in the past");
    }
    return futureMeeting;
  }
  
  /**
   * Returns the meeting with the specified ID or null if there is none.
   *
   * @param id The meeting ID.
   * @return The meeting with the requested ID or null if there is none.
   * @see Meeting
   */
  public Meeting getMeeting(int id) {
    Meeting meeting = futureMeetings.get(id);
    if (meeting == null)
      meeting = pastMeetings.get(id);
    return meeting;
  }
  
  /**
   * Returns a list of future meetings scheduled for this contact.
   *
   * If there are none, the returned list will be empty. Otherwise, it will be
   * chronologically sorted and will not contain any duplicates.
   *
   * @param contact The contact.
   * @return A list of future meeting(s) scheduled for this contact (can be
   *         empty).
   * @throws IllegalArgumentException If the contact does not exist.
   * @throws NullPointerException If the contact is null.
   * @see Contact
   * @see Meeting
   */
  public List<Meeting> getFutureMeetingList(Contact contact) {
    if (contact == null)
      throw new NullPointerException("contact must not be null");
    else if (!contacts.containsKey(contact.getId()))
      throw new IllegalArgumentException("contact must not be unknown");
    
    UpdateMeetings();
    
    // create a sorted set with a custom comparator than compares the dates
    // of two past meetings
    // by using a set we also remove duplicates
    TreeSet<Meeting> sortedSet = new TreeSet<>(new Comparator<Meeting>() {
      public int compare(Meeting m1, Meeting m2) {
        if (m1.equals(m2))
          return 0;
        return m1.getDate().compareTo(m2.getDate());
      }
    });
    
    // add the future meetings to the set
    for (FutureMeeting futureMeeting : futureMeetings.values()) {
      if (futureMeeting.getContacts().contains(contact))
        sortedSet.add(futureMeeting);
    }
    return new ArrayList<Meeting>(sortedSet);
  }
  
  /**
   * Returns a list of meetings scheduled for or that took place on the
   * specified date.
   *
   * If there are none, the returned list will be empty. Otherwise, it will be
   * sorted by id and will not contain any duplicates.
   *
   * @param date The date.
   * @return A list of meeting(s) schedule for or that took place on the
   *         specified date (can be empty).
   * @throws NullPointerException If the date is null.
   * @see Meeting
   */
  public List<Meeting> getMeetingListOn(Calendar date) {
    if (date == null)
      throw new NullPointerException("date must not be null");
    
    UpdateMeetings();
    
    // create a sorted set with a custom comparator than compares the IDs
    // of two meetings
    // by using a set we also remove duplicates
    TreeSet<Meeting> sortedSet = new TreeSet<>(new Comparator<Meeting>() {
      public int compare(Meeting m1, Meeting m2) {
        if (m1.equals(m2))
          return 0;
        return Integer.compare(m1.getId(), m2.getId());
      }
    });
    
    if (date.before(Calendar.getInstance())) {
      // add the past meetings to the set
      for (PastMeeting pastMeeting : pastMeetings.values()) {
        if (pastMeeting.getDate().equals(date))
          sortedSet.add(pastMeeting);
      }
    } else {
      // add the future meetings to the set
      for (FutureMeeting futureMeeting : futureMeetings.values()) {
        if (futureMeeting.getDate().equals(date))
          sortedSet.add(futureMeeting);
      }
    }
    return new ArrayList<Meeting>(sortedSet);
  }
  
  /**
   * Returns a list of past meetings in which this contact participated.
   *
   * If there are none, the returned list will be empty. Otherwise, it will be
   * chronologically sorted and will not contain any duplicates.
   *
   * @param contact The contact.
   * @return A list of past meeting(s) in which the contact participated (can
   *         be empty).
   * @throws IllegalArgumentException If the contact does not exist.
   * @throws NullPointerException If the contact is null.
   * @see Contact
   * @See PastMeeting
   */
  public List<PastMeeting> getPastMeetingListFor(Contact contact) {
    if (contact == null)
      throw new NullPointerException("contact must not be null");
    else if (!contacts.containsKey(contact.getId()))
      throw new IllegalArgumentException("contact must not be unknown");
    
    UpdateMeetings();
    
    // create a sorted set with a custom comparator than compares the dates
    // of two past meetings
    // by using a set we also remove duplicates
    TreeSet<PastMeeting> sortedSet = new TreeSet<>(
      new Comparator<PastMeeting>()
    {
      public int compare(PastMeeting m1, PastMeeting m2) {
        if (m1.equals(m2))
          return 0;
        return m1.getDate().compareTo(m2.getDate());
      }
    });
    
    // add the past meetings to the set
    for (PastMeeting pastMeeting : pastMeetings.values()) {
      if (pastMeeting.getContacts().contains(contact))
        sortedSet.add(pastMeeting);
    }
    return new ArrayList<PastMeeting>(sortedSet);
  }
  
  /**
   * Adds a new record for a meeting held in the past.
   *
   * @param contacts A set of participants.
   * @param date The date on which the meeting took place.
   * @param notes Notes to be added about the meeting.
   * @throws IllegalArgumentException If the list of contacts is empty or any
   *         of the contacts does not exist.
   * @throws NullPointerException If any of the arguments are null.
   * @see Contact
   * @see PastMeeting
   */
  public void addNewPastMeeting(Set<Contact> contacts, Calendar date,
                                String notes) {
    if (contacts == null || date == null || notes == null) {
      throw new NullPointerException(
        "contacts, date or notes must not be null");
    } else if (!this.contacts.values().containsAll(contacts)) {
      throw new IllegalArgumentException(
        "contacts must not be unknown or non-existent");
    } else if (date.compareTo(Calendar.getInstance()) >= 0) {
      throw new IllegalArgumentException(
        "date must be set for a time in the past");
    }
    
    // create past meeting
    PastMeetingImpl pastMeeting = new PastMeetingImpl(nextMeetingId, date,
      contacts, notes);
    
    // add meeting to map
    pastMeetings.put(nextMeetingId, pastMeeting);
    
    // increment next meeting ID
    nextMeetingId++;
  }
  
  /**
   * Adds notes to a meeting.
   *
   * This method is used when a future meeting takes place and is
   * then converted to a past meeting (with notes) and returned.
   *
   * It can be also used to add notes to a past meeting at a later date.
   *
   * @param id The meeting ID.
   * @param notes Meeting notes to be added.
   * @throws IllegalArgumentException If the meeting does not exist.
   * @throws IllegalStateException If the meeting is set for a date in the
   *         future.
   * @throws NullPointerException If the notes is null.
   * @see FutureMeeting
   * @see PastMeeting
   */
  public PastMeeting addMeetingNotes(int id, String notes) {
    if (notes == null)
      throw new NullPointerException("notes must not be null");
    
    PastMeetingImpl pastMeeting = pastMeetings.get(id);
    if (pastMeeting != null) {
      pastMeeting.addNotes(notes);
    } else {
      FutureMeetingImpl futureMeeting = futureMeetings.get(id);
      if (futureMeeting == null) {
        throw new IllegalArgumentException(
          "id must correspond to a known meeting");
      }
      
      // validate date
      Calendar date = futureMeeting.getDate();
      if (date.compareTo(Calendar.getInstance()) >= 0)
        throw new IllegalStateException("meeting hasn't take place yet");
      
      // remove from future meetings
      futureMeetings.remove(id);
      
      // create past meeting
      pastMeeting = new PastMeetingImpl(id, futureMeeting.getDate(),
        futureMeeting.getContacts(), notes);
      
      // add meeting to map
      pastMeetings.put(id, pastMeeting);
    }
    return pastMeeting;
  }
  
  /**
   * Adds a new contact with the specified name and notes.
   *
   * @param name Contact name.
   * @param notes Contact notes to be added.
   * @return The contact id.
   * @throws IllegalArgumentException If the name or the notes are empty
   *         strings.
   * @throws NullPointerException If the name or notes are null.
   * @see Contact
   */
  public int addNewContact(String name, String notes) {
    if (name.isEmpty() || notes.isEmpty())
      throw new IllegalArgumentException("name or notes must not be empty");
    
    // create contact
    Contact contact = new ContactImpl(nextContactId, name, notes);
    
    // add contact to set
    contacts.put(nextContactId, contact);
    
    // increment next contact ID
    return nextContactId++;
  }
  
  /**
   * Returns a set with the contacts whose name contains a specified string.
   *
   * If the specified string is empty, this methods returns the set that
   * contains all contacts.
   *
   * @param name The search string.
   * @return A list with the contacts whose name contains the specified string.
   * @throws NullPointerException If the name is null.
   * @see Contact
   */
  public Set<Contact> getContacts(String name) {
    if (name == null)
      throw new NullPointerException("name must not be null");
    
    Set<Contact> newContacts = new HashSet<Contact>();
    if (name.isEmpty()) {
      newContacts.addAll(contacts.values());
    } else {
      name = name.toLowerCase();
      
      for (Contact contact : contacts.values()) {
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
   * @param ids An arbitrary number of contact IDs.
   * @return A set containing the contacts that correspond to the IDs.
   * @throws IllegalArgumentException If no IDs are provided or if any of the
   *         provided IDs do not correspond to a real contact.
   * @see Contact
   */
  public Set<Contact> getContacts(int... ids) {
    if (ids == null)
      throw new NullPointerException("ids must not be null");
    
    Set<Contact> newContacts = new HashSet<Contact>();
    
    for (int id : ids) {
      Contact contact = contacts.get(id);
      if (contact != null)
        newContacts.add(contact);
    }
    
    if (newContacts.isEmpty()) {
      throw new IllegalArgumentException(
        "no id provided or non existing contact");
    }
    return newContacts;
  }
  
  /**
   * Returns the contact that correspond to the ID.
   *
   * @param id The contact ID.
   * @return The contact.
   * @throws IllegalArgumentException If the ID does not correspond to a known
   *         contact.
   * @see Contact
   */
  public Contact getContact(int id) {
    Contact contact = contacts.get(id);
    if (contact == null) {
      throw new IllegalArgumentException(
        "id must correspond to a known contact");
    }
    return contact;
  }
  
  /**
   * Saves all data to disk.
   *
   * This method must be executed when the program is
   * closed and when/if the user requests it.
   */
  public void flush() {
    XMLOutputFactory factory = XMLOutputFactory.newInstance();
    XMLStreamWriter writer = null;
    
    // Write data as XML to disk
    try {
      writer = factory.createXMLStreamWriter(new FileOutputStream(file));
      
      writer.writeStartDocument("utf-8", "1.0");
      serialise(writer);
      writer.writeEndDocument();
      
      writer.flush();
      writer.close();
    } catch (Exception e) {
      // The interface does not specify what to do in case of errors when
      // flushing the data so we ignore them
    } finally {
      if (writer != null) {
        try { writer.close(); } catch (Exception e) {}
      }
    }
  }
  
  // Loops thru future meetings looking for meetings that already took place
  // and converts them to past meetings.
  private void UpdateMeetings() {
    for (FutureMeeting futureMeeting : futureMeetings.values()) {
      // validate date
      Calendar date = futureMeeting.getDate();
      if (date.compareTo(Calendar.getInstance()) >= 0)
        continue;
      
      // remove from future meetings
      int id = futureMeeting.getId();
      futureMeetings.remove(id);
      
      // create past meeting
      PastMeetingImpl pastMeeting = new PastMeetingImpl(id,
        futureMeeting.getDate(), futureMeeting.getContacts(), "");
      
      // add meeting to map
      pastMeetings.put(id, pastMeeting);
    }
  }
  
  // Serialises the contact manager data as XML.
  private void serialise(XMLStreamWriter writer) throws XMLStreamException {
    writer.writeStartElement("ContactManager");
    
    serialiseContacts(writer);
    serialiseMeetings(writer);
    
    writer.writeEndElement();
  }
  
  // Serialises the contacts.
  private void serialiseContacts(XMLStreamWriter writer)
    throws XMLStreamException
  {
    writer.writeStartElement("Contacts");
    
    for (Contact contact : contacts.values()) {
      writer.writeStartElement("Contact");
      writer.writeAttribute("id", Integer.toString(contact.getId()));
      
      writer.writeStartElement("Name");
      writer.writeCharacters(contact.getName());
      writer.writeEndElement();
      
      writer.writeStartElement("Notes");
      writer.writeCharacters(contact.getNotes());
      writer.writeEndElement();
      
      writer.writeEndElement();
    }
    
    writer.writeEndElement();
  }
  
  // Serialises the meetings.
  private void serialiseMeetings(XMLStreamWriter writer)
    throws XMLStreamException
  {
    writer.writeStartElement("Meetings");
    
    for (PastMeeting pastMeeting : pastMeetings.values())
      serialiseMeeting(writer, pastMeeting);
    
    for (FutureMeeting futureMeeting : futureMeetings.values())
      serialiseMeeting(writer, futureMeeting);
    
    writer.writeEndElement();
  }
  
  // Serialises a meeting.
  private void serialiseMeeting(XMLStreamWriter writer, Meeting meeting)
    throws XMLStreamException
  {
    writer.writeStartElement("Meeting");
    writer.writeAttribute("id", Integer.toString(meeting.getId()));
    
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    writer.writeStartElement("Date");
    writer.writeCharacters(formatter.format(meeting.getDate().getTime()));
    writer.writeEndElement();
    
    if (meeting instanceof PastMeeting) {
      PastMeeting pastMeeting = (PastMeeting)meeting;
      writer.writeStartElement("Notes");
      writer.writeCharacters(pastMeeting.getNotes());
      writer.writeEndElement();
    }
    
    writer.writeStartElement("Contacts");
    Set<Contact> contacts = meeting.getContacts();
    for (Contact contact : contacts) {
      writer.writeStartElement("Id");
      writer.writeCharacters(Integer.toString(contact.getId()));
      writer.writeEndElement();
    }
    writer.writeEndElement();
    
    writer.writeEndElement();
  }
  
  // Loads the contact manager data from XML.
  private void load() {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    XMLStreamReader reader = null;
    
    // Write data as XML to disk
    try {
      reader = factory.createXMLStreamReader(new FileInputStream(file));
      
      // Skip start of document and validate root object
      reader.next();
      if (reader.getLocalName().equals("ContactManager"))
        loadDocument(reader);
    } catch (Exception e) {
      // The interface does not specify what to do in case of errors when
      // loading the data so we ignore them
    } finally {
      if (reader != null) {
        try { reader.close(); } catch (Exception e) {}
      }
    }
  }
  
  // Loads the contacts and meetings.
  private void loadDocument(XMLStreamReader reader) throws XMLStreamException {
    boolean contactsLoaded = false;
    boolean meetingsLoaded = false;
    
    while (reader.hasNext()) {
      switch (reader.next()) {
        case XMLStreamReader.START_ELEMENT:
          String elementName = reader.getLocalName();
          if (elementName.equals("Contacts")) {
            if (!contactsLoaded) {
              loadContacts(reader);
              contactsLoaded = true;
            }
          } else if (elementName.equals("Meetings")) {
            if (!meetingsLoaded) {
              loadMeetings(reader);
              meetingsLoaded = true;
            }
          }
          break;
        case XMLStreamReader.END_ELEMENT:
          break;
      }
    }
  }
  
  private void loadContacts(XMLStreamReader reader) throws XMLStreamException {
    while (reader.hasNext()) {
      switch (reader.next()) {
        case XMLStreamReader.START_ELEMENT:
          String name = reader.getLocalName();
          if (name.equals("Contact"))
            loadContact(reader);
          break;
        case XMLStreamReader.END_ELEMENT:
          return;
      }
    }
  }
  
  private void loadContact(XMLStreamReader reader) throws XMLStreamException {
    String name = null, notes = null;
    int id = -1;
    
    try {
      id = Integer.parseInt(reader.getAttributeValue(null, "id"));
    } catch (Exception e) {
      return;
    }
    
    boolean done = false;
    while (!done && reader.hasNext()) {
      switch (reader.next()) {
        case XMLStreamReader.START_ELEMENT:
          String elementName = reader.getLocalName();
          if (elementName.equals("Name") && name == null)
            name = readText(reader);
          else if (elementName.equals("Notes") && notes == null)
            notes = readText(reader);
          break;
        case XMLStreamReader.END_ELEMENT:
          done = true;
          break;
      }
    }
    
    if (id > 0 && name != null) {
      if (notes == null)
        notes = "";
      
      // Add contact
      contacts.put(id, new ContactImpl(id, name, notes));
      
      // Update next contact id
      id++;
      if (id > nextContactId)
        nextContactId = id;
    }
  }
  
  private void loadMeetings(XMLStreamReader reader) throws XMLStreamException {
    while (reader.hasNext()) {
      switch (reader.next()) {
        case XMLStreamReader.START_ELEMENT:
          String name = reader.getLocalName();
          if (name.equals("Meeting"))
            loadMeeting(reader);
          break;
        case XMLStreamReader.END_ELEMENT:
          break;
      }
    }
  }
  
  private void loadMeeting(XMLStreamReader reader) throws XMLStreamException {
    Set<Contact> contacts = null;
    Calendar date = null;
    String notes = null;
    int id = -1;
    
    try {
      id = Integer.parseInt(reader.getAttributeValue(null, "id"));
    } catch (Exception e) {
      return;
    }
    
    boolean done = false;
    while (!done && reader.hasNext()) {
      switch (reader.next()) {
        case XMLStreamReader.START_ELEMENT:
          String elementName = reader.getLocalName();
          if (elementName.equals("Date") && date == null)
            date = readDate(reader);
          else if (elementName.equals("Notes") && notes == null)
            notes = readText(reader);
          else if (elementName.equals("Contacts") && contacts == null)
            contacts = readMeetingContacts(reader);
          break;
        case XMLStreamReader.END_ELEMENT:
          done = true;
          break;
      }
    }
    
    if (id > 0 && date != null && contacts != null && !contacts.isEmpty()) {
      if (notes == null)
        notes = "";
      
      if (date.before(Calendar.getInstance())) {
        // Add past meeting
        pastMeetings.put(id, new PastMeetingImpl(id, date, contacts, notes));
      } else {
        // Add future meeting
        futureMeetings.put(id, new FutureMeetingImpl(id, date, contacts));
      }
      
      // Update next meeting id
      id++;
      if (id > nextMeetingId)
        nextMeetingId = id;
    }
  }
  
  // Reads element characters.
  private String readText(XMLStreamReader reader) throws XMLStreamException {
    StringBuilder result = new StringBuilder();
    boolean done = false;
    
    while (!done && reader.hasNext()) {
      switch (reader.next()) {
        case XMLStreamReader.CHARACTERS:
          result.append(reader.getText());
          break;
        case XMLStreamReader.END_ELEMENT:
          done = true;
          break;
      }
    }
    return result.toString();
  }
  
  // Reads element as Calendar.
  private Calendar readDate(XMLStreamReader reader) throws XMLStreamException {
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    try {
      Calendar date = Calendar.getInstance();
      date.setTime(formatter.parse(readText(reader)));
      return date;
    } catch (Exception e) {
      return null;
    }
  }
  
  // Reads the set of contacts for a meeting.
  private Set<Contact> readMeetingContacts(XMLStreamReader reader)
    throws XMLStreamException
  {
    Set<Contact> contacts = new HashSet<>();
    
    boolean done = false;
    while (!done && reader.hasNext()) {
      switch (reader.next()) {
        case XMLStreamReader.CHARACTERS:
          contacts.add(readMeetingContact(reader));
          break;
        case XMLStreamReader.END_ELEMENT:
          done = true;
          break;
      }
    }
    return contacts;
  }
  
  // Reads one contact in a meeting.
  private Contact readMeetingContact(XMLStreamReader reader)
    throws XMLStreamException
  {
    String string = null;
  
    boolean done = false;
    while (!done && reader.hasNext()) {
      switch (reader.next()) {
        case XMLStreamReader.START_ELEMENT:
          String elementName = reader.getLocalName();
          if (elementName.equals("Id"))
            string = readText(reader);
          break;
        case XMLStreamReader.END_ELEMENT:
          done = true;
          break;
      }
    }
    
    if (string != null) {
      try {
        int id = Integer.parseInt(reader.getAttributeValue(null, "id"));
        return contacts.get(id);
      } catch (Exception e) {
      }
    }
    return null;
  }
}
