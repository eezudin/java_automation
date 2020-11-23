package ru.stqa.pft.mantis.appmanager;

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
  private WebDriver wd;
  private final String browser;
  private HttpSession registrationHelper;
  private FtpHelper ftp;
  private MailHelper mailHelper;
  private JamesHelper JamesHelper;

  public ApplicationManager(String browser) {
    this.browser = browser;
    properties = new Properties();
  }

  public void init() throws IOException {
    String target = System.getProperty("target", "local");
    properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target))));
  }

  public void stop() {
    if (wd != null) {
      wd.quit();
    }
  }

  public HttpSession newSession() {
    if (registrationHelper == null) {
      registrationHelper = new HttpSession(this);
    }
   return registrationHelper;
  }

  public String getProperty(String key) {
    return properties.getProperty(key);
  }

  public RegistrationHelper registration() {
    return new RegistrationHelper(this);
  }

  public FtpHelper ftp() {
    if (ftp == null) {
      ftp = new FtpHelper(this);
    }
    return  ftp;
  }

  public WebDriver getDriver() {
    if (wd == null) {
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
      wd.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
      wd.get(properties.getProperty("web.baseUrl"));
    }
    return wd;
  }

  public MailHelper mail() {
    if (mailHelper == null) {
      mailHelper = new MailHelper(this);
    }
    return mailHelper;
  }

  public JamesHelper james() {
    if (JamesHelper == null) {
      JamesHelper = new JamesHelper(this);
    }
    return JamesHelper;
  }
}
