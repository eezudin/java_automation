package ru.stqa.pft.soap;
import com.lavasoft.GeoIPService;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

import static org.testng.Assert.assertTrue;

public class GeoIpServiceTests {

  @Test
  public void testMyIp() {
    String geoIp = new GeoIPService().getGeoIPServiceSoap12().getIpLocation("89.109.44.203");
    assertTrue(Pattern.compile("<Country>RU</Country>").matcher(geoIp).find());
  }
}
