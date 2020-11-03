package ru.stqa.pft.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.*;
import com.thoughtworks.xstream.XStream;
import ru.stqa.pft.addressbook.model.ContactData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ContactDataGenerator {

  @Parameter(names = "-c", description = "Contact count")
  public int count;

  @Parameter(names = "-f", description = "Target file")
  public String file;

  @Parameter(names = "-d", description = "Data format")
  public String format;

  public static void main(String[] args) throws IOException {
    ContactDataGenerator generator = new ContactDataGenerator();
    JCommander jc = new JCommander(generator);
    try {
      jc.parse(args);
    } catch (ParameterException ex) {
      jc.usage();
    }
    generator.run();
  }

  private void run() throws IOException {
    List<ContactData> contacts = generate(count);
    switch (format) {
      case "csv":
        saveAsCsv(contacts, new File(file));
        break;
      case "xml":
        saveAsXml(contacts, new File(file));
        break;
      case "json":
        saveAsJson(contacts, new File(file));
        break;
      default:
        System.out.println("Unrecognized format " + format);
        break;
    }

  }

  private void saveAsJson(List<ContactData> contacts, File file) throws IOException {
    GsonBuilder gsonBuilder = new GsonBuilder();
    JsonSerializer<File> serializer = (src, typeOfSrc, context) -> new JsonPrimitive(src.getPath());
    gsonBuilder.registerTypeAdapter(File.class, serializer);
    Gson gson = gsonBuilder.setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    String json = gson.toJson(contacts);
    Writer writer = new FileWriter(file);
    writer.write(json);
    writer.close();
  }

  private void saveAsXml(List<ContactData> contacts, File file) throws IOException {
    XStream xstream = new XStream();
    xstream.processAnnotations(ContactData.class);
    String xml = xstream.toXML(contacts);
    Writer writer = new FileWriter(file);
    writer.write(xml);
    writer.close();
  }

  private void saveAsCsv(List<ContactData> contacts, File file) throws IOException {
    System.out.println(new File(".").getAbsolutePath());
    Writer writer = new FileWriter(file);
    for(ContactData contact : contacts) {
      writer.write(String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s\n", contact.getFirstName(), contact.getLastName(),
              contact.getMobilePhone(), contact.getHomePhone(), contact.getWorkPhone(),
              contact.getEmail(), contact.getEmail3(), contact.getGroup(), contact.getAddress(),
              contact.getPhoto()));
    }
    writer.close();
  }

  private List<ContactData> generate(int count) {
    List<ContactData> contacts = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      contacts.add(new ContactData()
              .withFirstName(String.format("test_first_name_%s", i))
              .withLastName(String.format("test_last_name_%s", i))
              .withMobilePhone("99025522208").withHomePhone("546546546").withWorkPhone("31231314564")
              .withEmail("batman@test.com").withEmail3("batman@3.com").withGroup("test1").withAddress("Gotham")
              .withPhoto(new File("src/test/resources/pic.jpg")));
    }
    return contacts;
  }










}


