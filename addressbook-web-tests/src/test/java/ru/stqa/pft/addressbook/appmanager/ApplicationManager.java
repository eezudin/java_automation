package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ApplicationManager {
  private final Properties properties;
  protected WebDriver wd;

  private ContactHelper contactHelper;
  private GroupHelper groupHelper;
  private NavigationHelper navigationHelper;
  private SessionHelper sessionHelper;
  private final String browser;

  public ApplicationManager(String browser) {
    this.browser = browser;
    properties = new Properties();
  }

  public void init() throws IOException {
    String target = System.getProperty("target", "local");
    properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target))));

    switch (browser) {
      case BrowserType.FIREFOX:
        wd = new FirefoxDriver();
        break;
      case BrowserType.CHROME:
        wd = new ChromeDriver();
        break;
      case BrowserType.IE:
        wd = new InternetExplorerDriver();
        break;
    }

    contactHelper = new ContactHelper(wd);
    groupHelper = new GroupHelper(wd);
    navigationHelper = new NavigationHelper(wd);
    sessionHelper = new SessionHelper(wd);

    wd.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    wd.get(properties.getProperty("web.baseUrl"));
    sessionHelper.login(properties.getProperty("web.adminLogin"), properties.getProperty("web.adminPassword"));
  }

  public void stop() {
    sessionHelper.logout();
    wd.quit();
  }

  public ContactHelper contact() {
    return contactHelper;
  }
  public GroupHelper group() {
    return groupHelper;
  }

  public NavigationHelper goTo() {
    return navigationHelper;
  }
}
