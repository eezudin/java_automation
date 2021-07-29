package ru.stqa.pft.soap;

import com.lavasoft.GeoIPService;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Pattern;

import static org.testng.Assert.assertTrue;

public class GeoIpServiceTests {

  @Test
  public void testMyIp() {
    String geoIp = new GeoIPService().getGeoIPServiceSoap12().getIpLocation(getSystemIP());
    assertTrue(Pattern.compile("<Country>RU</Country>").matcher(geoIp).find());
  }


  private String getSystemIP() {
    // Find public IP address
    String systemIpAddress;
    try {
      URL url_name = new URL("http://bot.whatismyipaddress.com");

      try (BufferedReader sc = new BufferedReader(new InputStreamReader(url_name.openStream()))) {

        // reads system IPAddress
        systemIpAddress = sc.readLine().trim();
      }
    } catch (Exception e) {
      systemIpAddress = "Cannot Execute Properly";
    }
    return systemIpAddress;
  }

}
