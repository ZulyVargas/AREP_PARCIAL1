package edu.escuelaing.arep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class ClimaApi {

    public static String consultaClimaCiudad(String ciudad) throws MalformedURLException {

        URL url = new URL("http://api.openweathermap.org/data/2.5/find?q="+ciudad+"&appid=67c2460437f341124e705763190d0586");
        String result = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String inputLine = null;
                while ((inputLine = reader.readLine()) != null) {
                    result = result + inputLine;
                }
            } catch (IOException x) {
            System.err.println(x);
            }
        System.out.println("RESULTADO "+ result );
        return result;
    }
}
