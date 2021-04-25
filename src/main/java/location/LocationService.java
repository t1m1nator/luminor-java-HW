package location;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

public class LocationService {
    private DatabaseReader dbReader;

    public LocationService() throws IOException {
        File database = new File("src/main/resources/GeoLite2-Country_20210420/GeoLite2-Country.mmdb");
        dbReader = new DatabaseReader.Builder(database).build();
    }

    public String getCountryLocation(String ip) throws IOException, GeoIp2Exception {
        InetAddress ipAddress = InetAddress.getByName(ip);
        CountryResponse response = dbReader.country(ipAddress);

        return response.getCountry().toString();
    }
}
