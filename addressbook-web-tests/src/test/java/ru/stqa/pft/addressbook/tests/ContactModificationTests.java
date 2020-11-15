package ru.stqa.pft.addressbook.tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactModificationTests extends TestBase {

  @DataProvider
  public Iterator<Object[]> validContactsFromJson() throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/contacts.json")))) {

      String json = "";
      String line = reader.readLine();
      while (line != null) {
        json += line;
        line = reader.readLine();
      }

      GsonBuilder gsonBuilder = new GsonBuilder();

      JsonDeserializer<ContactData> deserializer = (json1, typeOfT, context) -> {
        JsonObject jsonObject = json1.getAsJsonObject();

        File file = new File(
                jsonObject.get("photo").getAsString()
        );

        if (app.db().groups().size() == 0) {
          app.goTo().groupPage();
          app.group().create(new GroupData().withName("test1"));
        }

        return new ContactData()
                .withFirstName(jsonObject.get("firstName").getAsString())
                .withLastName(jsonObject.get("lastName").getAsString())
                .withMobilePhone(jsonObject.get("mobilePhone").getAsString())
                .withHomePhone(jsonObject.get("homePhone").getAsString())
                .withWorkPhone(jsonObject.get("workPhone").getAsString())
                .withEmail(jsonObject.get("email").getAsString())
                .withEmail3(jsonObject.get("email3").getAsString())
                .inGroup(app.db().groups().iterator().next())
                .withAddress(jsonObject.get("address").getAsString())
                .withPhoto(file);
      };
      gsonBuilder.registerTypeAdapter(ContactData.class, deserializer);
      Gson gson = gsonBuilder.create();

      List<ContactData> contacts = gson.fromJson(json, new TypeToken<List<ContactData>>() {
      }.getType()); // List<ContactData>.class
      return contacts.stream().map((c) -> new Object[]{c}).collect(Collectors.toList()).iterator();
    }
  }

  @BeforeMethod
  public static void runPreconditions() {
    app.goTo().homePage();
    if (app.db().contacts().size() == 0) {
      app.contact().create(
              new ContactData().withFirstName("Batman").withLastName("Batman")
                      .withMobilePhone("99025522208").withHomePhone("21348").withWorkPhone("54218465")
                      .withEmail("batman@test.com").withEmail3("btmn@test.com")
                      .withPhoto(new File("src/test/resources/pic.jpg"))
                      .inGroup(app.db().groups().iterator().next())
                      .withAddress("Gotham"));
    }
  }

  @Test(dataProvider = "validContactsFromJson")
  public void testContactModification(ContactData contact) {
    Contacts before = app.db().contacts();
    ContactData modifiedContact = before.iterator().next();
    app.contact().modify(contact.withId(modifiedContact.getId()));
    assertThat(app.contact().count(), equalTo(before.size()));
    Contacts after = app.db().contacts();
    assertThat(after, equalTo(before.withModified(contact)));
    verifyContactListInUI();
  }
}
