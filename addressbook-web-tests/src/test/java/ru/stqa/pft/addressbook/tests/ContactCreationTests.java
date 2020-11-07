package ru.stqa.pft.addressbook.tests;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactCreationTests extends TestBase {

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

        return new ContactData()
                .withFirstName(jsonObject.get("firstName").getAsString())
                .withLastName(jsonObject.get("lastName").getAsString())
                .withMobilePhone(jsonObject.get("mobilePhone").getAsString())
                .withHomePhone(jsonObject.get("homePhone").getAsString())
                .withWorkPhone(jsonObject.get("workPhone").getAsString())
                .withEmail(jsonObject.get("email").getAsString())
                .withEmail3(jsonObject.get("email3").getAsString())
                .withGroup(jsonObject.get("group").getAsString())
                .withAddress(jsonObject.get("address").getAsString())
                .withPhoto(file);
      };
      gsonBuilder.registerTypeAdapter(ContactData.class, deserializer);
      Gson gson = gsonBuilder.create();

      List<ContactData> contacts = gson.fromJson(json, new TypeToken<List<ContactData>>() {}.getType()); // List<ContactData>.class
      return contacts.stream().map((c) -> new Object[]{c}).collect(Collectors.toList()).iterator();
    }
  }

  @Test(dataProvider = "validContactsFromJson")
  public void testContactCreation(ContactData contact) {
    app.goTo().homePage();
    Contacts before = app.db().contacts();
    app.contact().create(contact);
    assertThat(app.contact().count(), equalTo((before.size() + 1)));
    Contacts after = app.db().contacts();
    assertThat(after, equalTo(before.withAdded(contact.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt()))));
    verifyContactListInUI();
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
