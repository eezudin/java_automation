package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactAddressTests extends TestBase {

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

  @Test(invocationCount = 3)
  public void testContactAddress() {
    app.goTo().homePage();
    ContactData contact = app.db().contacts().iterator().next();
    ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(contact);
    assertThat(contact.getAddress(), equalTo(contactInfoFromEditForm.getAddress()));
  }
}
