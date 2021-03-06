package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static ru.stqa.pft.addressbook.tests.TestBase.app;

public class ContactHelper extends HelperBase {

  public ContactHelper(WebDriver wd) {
    super(wd);
  }

  public void submitContactCreation() {
    click(By.xpath("(//input[@name='submit'])[2]"));
  }

  public void fillContactForm(ContactData contactData, boolean creation) {
    type(By.name("firstname"), contactData.getFirstName());
    type(By.name("lastname"), contactData.getLastName());
    type(By.name("home"), contactData.getHomePhone());
    type(By.name("mobile"), contactData.getMobilePhone());
    type(By.name("work"), contactData.getWorkPhone());
    type(By.name("email"), contactData.getEmail());
    type(By.name("email2"), contactData.getEmail2());
    type(By.name("email3"), contactData.getEmail3());
    type(By.name("address"), contactData.getAddress());
    type(By.name("photo"), contactData.getPhoto().getAbsolutePath());

    if (creation && contactData.getGroups().size() > 0) {
      Assert.assertTrue(contactData.getGroups().size() == 1);
      new Select(wd.findElement(By.name("new_group"))).selectByVisibleText(contactData.getGroups().iterator().next().getName());
    } else {
      Assert.assertFalse(isElementPresent(By.name("new_group")), "This select should not be present");
    }
  }

  public void returnToHomePage() {
    click(By.linkText("home page"));
  }

  public void editContact(int index) {
    wd.findElements(By.xpath("//img[@alt='Edit']")).get(index).click();
  }

  public void deleteSelectedContacts() {
    click(By.xpath(".//input[@value ='Delete']"));
  }

  public boolean isContactExist() {
    return isItemExist();
  }

  public void initContactCreation() {
    click(By.xpath(".//a[.='add new']"));
  }

  public void create(ContactData contactData) {
    initContactCreation();
    fillContactForm(contactData, true);
    submitContactCreation();
    cashedContacts = null;
    returnToHomePage();
  }

  public void modify(ContactData contact) {
    editContactById(contact.getId());
    fillContactForm(contact, false);
    submitModification();
    cashedContacts = null;
    returnToHomePage();
  }

  public void delete(ContactData contact) {
    markContactById(contact.getId());
    deleteSelectedContacts();
    acceptAlert();
    cashedContacts = null;
  }

  private void editContactById(int id) {
    // wd.findElement(By.xpath("//input[@value='" + id + "']/../following-sibling::td/a/img[@Title='Edit']")).click();
    wd.findElement(By.xpath(String.format("//input[@value='%s']/../following-sibling::td/a/img[@Title='Edit']", id))).click();
  }

  public void markContactById(int id) {
    wd.findElement(By.cssSelector("input[value='" + id + "']")).click();
  }

  private Contacts cashedContacts = null;

  public Contacts all() {
    if (cashedContacts != null) {
      return new Contacts(cashedContacts);
    }
    Contacts cashedContacts = new Contacts();
    List<WebElement> rows = wd.findElements(By.cssSelector("tr[name = entry]"));
    for (WebElement row : rows) {
      int id = Integer.parseInt(row.findElement(By.tagName("input")).getAttribute("value"));
      List<WebElement> cells = row.findElements(By.tagName("td"));
      String allPhones = cells.get(5).getText();
      String allEmails = cells.get(4).getText();
      String address = cells.get(3).getText();
      cashedContacts.add(new ContactData().withId(id).withFirstName(cells.get(2).getText()).withLastName(cells.get(1).
              getText()).withAllPhones(allPhones).withAllEmails(allEmails).withAddress(address));
    }
    return new Contacts(cashedContacts);
  }

  public int count() {
    return wd.findElements(By.name("selected[]")).size();
  }

  public ContactData infoFromEditForm(ContactData contact) {
    editContactById(contact.getId());
    String firstName = wd.findElement(By.name("firstname")).getAttribute("value");
    String lastName = wd.findElement(By.name("lastname")).getAttribute("value");
    String home = wd.findElement(By.name("home")).getAttribute("value");
    String mobile = wd.findElement(By.name("mobile")).getAttribute("value");
    String work = wd.findElement(By.name("work")).getAttribute("value");
    String email = wd.findElement(By.name("email")).getAttribute("value");
    String email2 = wd.findElement(By.name("email2")).getAttribute("value");
    String email3 = wd.findElement(By.name("email3")).getAttribute("value");
    String address = wd.findElement(By.name("address")).getAttribute("value");
    wd.navigate().back();
    return new ContactData().withFirstName(firstName).withLastName(lastName).withHomePhone(home).withMobilePhone(mobile)
            .withWorkPhone(work).withEmail(email).withEmail2(email2).withEmail3(email3).withAddress(address);
  }

  public String addToCorrectGroup(ContactData contact) {
    String selectedGroup = null;
    List<String> groupsNames = contact.getGroups().stream().map(GroupData::getName).collect(Collectors.toList());
    List<String> options = new Select(wd.findElement(By.name("to_group"))).getOptions().stream().map(WebElement::getText)
            .filter(o -> !groupsNames.contains(o)).collect(Collectors.toList());
    try {
      selectedGroup = options.iterator().next();
      new Select(wd.findElement(By.name("to_group"))).selectByVisibleText(selectedGroup);
    } catch (NoSuchElementException e) {
      e.printStackTrace();
      System.out.println("User is a member of all groups");
      app.goTo().groupPage();
      String groupName = "test_group_from_catch_" + Math.random() * 0xfffff * 1_000_000;
      app.group().create(new GroupData().withName(groupName));
      app.goTo().homePage();
      markContactById(contact.getId());
      new Select(wd.findElement(By.name("to_group"))).selectByVisibleText(groupName);
      selectedGroup = groupName;
    } finally {
      wd.findElement(By.name("add")).click();
      wd.findElement(By.cssSelector(".msgbox a")).click();
    }
    return selectedGroup;
  }

  public void addToAnyGroup(ContactData contact) {
    String option = new Select(wd.findElement(By.name("to_group"))).getOptions().stream().iterator().next().getText();
    new Select(wd.findElement(By.name("to_group"))).selectByVisibleText(option);
    markContactById(contact.getId());
    wd.findElement(By.name("add")).click();
    wd.findElement(By.cssSelector(".msgbox a")).click();
  }

  public ContactData withAnyGroup() {
    ContactData anyContact = app.db().contacts().stream().iterator().next();
    ContactData contactWithGroup = null;
    try {
      contactWithGroup = app.db().contacts().stream().filter(c -> c.getGroups().size() > 0).iterator().next();
    } catch (NoSuchElementException e) {
      e.printStackTrace();
      System.out.println("All users are not members of any group");
      app.goTo().homePage();
      addToAnyGroup(anyContact);
      contactWithGroup = anyContact;
    }
    return contactWithGroup;
  }
}
