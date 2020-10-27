package ru.stqa.pft.addressbook.model;

import com.google.common.collect.ForwardingSet;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Contacts extends ForwardingSet<ContactData>  {
  private final Set<ContactData> delegate;

  public Contacts(Contacts contacts) {
    this.delegate = new HashSet<>(contacts.delegate);
  }

  public Contacts() {
    this.delegate = new HashSet<>();
  }

  @Override
  protected Set<ContactData> delegate() {
    return delegate;
  }

  public Contacts withAdded(ContactData contact) {
    Contacts contacts = new Contacts(this);
    contacts.add(contact);
    return contacts;
  }

  public Contacts without(ContactData contact) {
    Contacts contacts = new Contacts(this);
    contacts.remove(contact);
    return contacts;
  }

  public Contacts withModified(ContactData contact) {
    Contacts contacts = new Contacts(this);
    Optional<ContactData> matchedContact = this.stream().filter(x -> x.getId() == contact.getId()).findFirst();
    contacts.remove(matchedContact.get());
    contacts.add(contact);
    return contacts;
  }
}
