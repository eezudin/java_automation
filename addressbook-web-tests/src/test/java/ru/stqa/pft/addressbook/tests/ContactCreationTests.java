package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactCreationTests extends TestBase {

  @Test
  // @Test(invocationCount = 5)
  public void testContactCreation() {
    app.goTo().homePage();
    Contacts before = app.contact().all();
    File photo = new File("src/test/resources/pic.jpg");
    ContactData contact = new ContactData().withFirstName("Batman").withLastName("Batman")
            .withMobilePhone("99025522208").withHomePhone("546546546").withWorkPhone("31231314564")
            .withEmail("batman@test.com").withEmail3("batman@3.com").withGroup("test1").withAddress("Gotham")
            .withPhoto(photo);
    app.contact().create(contact);
    assertThat(app.contact().count(), equalTo((before.size() + 1)));
    Contacts after = app.contact().all();
    assertThat(after, equalTo(before.withAdded(contact.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt()))));
  }

  @Test(enabled = false)
  public void testCurrentDir() {
    File currentDir = new File(".");
    System.out.println("absolute path: " + currentDir.getAbsolutePath());
    File photo = new File("src/test/resources/pic.jpg");
    System.out.println("photo absolute path: " + photo.getAbsolutePath());
    System.out.println(photo.exists());
  }
}
