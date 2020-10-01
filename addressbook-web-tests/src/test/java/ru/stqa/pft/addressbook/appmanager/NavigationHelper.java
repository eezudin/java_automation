package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NavigationHelper extends HelperBase {

  public NavigationHelper(WebDriver wd) {
    super(wd);
  }

  public void goToGroupPage() {
    click(By.linkText("groups"));
  }

  public void goToCreateContactPage() {
    click(By.xpath(".//a[.='add new']"));
  }

  public void goToHomePage() {
    click(By.xpath(".//a[.='home']"));
  }
}