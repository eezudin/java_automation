package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactAdditionInGroupTests extends TestBase {

  @BeforeMethod
  public static void runPreconditions() {
    if (app.db().groups().size() == 0) {
      app.goTo().groupPage();
      app.group().create(new GroupData().withName("test1"));
    }
    app.goTo().homePage();
    if (app.db().contacts().size() == 0) {
      app.contact().create(new ContactData().withFirstName("Batman").withLastName("Batman").withHomePhone("531245")
              .withMobilePhone("99025522208").withWorkPhone("35123").withEmail("batman@test.com")
              .withPhoto(new File("src/test/resources/pic.jpg"))
              .withEmail2("batman@test.ru").inGroup(app.db().groups().iterator().next()).withAddress("Gotham"));
    }
  }

  @Test
  public void testContactAdditionInGroup() {
    app.goTo().homePage();
    ContactData contact = app.db().contacts().iterator().next();
    int contactId = contact.getId();
    app.contact().markContactById(contactId);
    String selectedGroup = app.contact().addToCorrectGroup(contact);
    List<String> contactsGroups = app.db().groupsByContactId(contactId).stream().map(GroupData::getName).collect(Collectors.toList());
    assertThat(contactsGroups, hasItem(selectedGroup));
  }
}
