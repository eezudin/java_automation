package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactDeletionFromGroupTests extends TestBase {
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
  public void testContactDeletionFromGroup() {
    app.goTo().homePage();
    ContactData contact = app.contact().withAnyGroup();
    GroupData groupName = app.db().groupsByContactId(contact.getId()).iterator().next();
    app.group().selectGroupByName(groupName.getName());
    app.contact().markContactById(contact.getId());
    app.group().removeFromGroup();
    List<GroupData> contactsGroups = app.db().groupsByContactId(contact.getId()).stream().collect(Collectors.toList());
    assertThat(contactsGroups, not(hasItem(groupName)));
  }
}
