package edu.escuelaing.arep;

import java.net.*;
import java.io.*;



public class HttpServer {

    public static void  main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(getPort());
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine="";
            String file = "";
            boolean primeraLinea = true;
            while ((inputLine = in.readLine()) != null) {
                if (primeraLinea) {
                    file = inputLine.split(" ")[1];
                    System.out.println("File: " + file);
                    primeraLinea = false;
                }
                 System.out.println("Received: " + inputLine);
                 System.out.println("FILE " + file);
                if (!in.ready()) {
                    break;
                }
            }

                if (file.startsWith("/clima")){
                    outputLine = "HTTP/1.1 200 OK\r\n"
                            +"Access-Control-Allow-Origin: *"
                            + "Content-Type: text/html\r\n"
                            + "\r\n"
                            + "<!DOCTYPE html>"
                            + "<html>"
                            + "<head>"
                            + "<meta charset=\"UTF-8\">"
                            + "<title>API del Clima</title>\n"
                            + "</head>"
                            + "<body>"
                            + "Clima API"
                            +"<input type='text' class='form-control' placeholder='Ciudad' name='ciudad' id='ciudad'  >"
                            +"<input id='boton-ciudad' type='button' value='CONSULTAR' onclick='traerClima()' >"
                            +"<p  id='ciudadResult' ><b></b></p>"
                            +"<script>"
                                 +"const url1 = 'https://clima1api.herokuapp.com/consulta?lugar=';"
                            +"const valueCiudad = document.getElementById('ciudad');  "
                            +"const button = document.getElementById('boton-ciudad');"
                            +"async function traerClima(){ \n"
                            +"const ans = await fetchData(valueCiudad.value);"
                            +"const res = JSON.stringify(ans);"
                            +" ciudadResult.innerHTML = `${res}`;"
                            +"}"
                            +"async function fetchData(value){  \n"
                            +"const url = `${url1}${value}`;"
                            +"console.log('Url en fecth', url);\n"
                            +"const res = await fetch(url, { \n"
                            +" method: 'GET',\n"
                            +"headers: {\n"
                            +"'Content-Type': 'application/json'"
                            +" }"
                            +"});"
                            + "const result = await res.json();\n" +
                            "return result;\n"
                            +"}"
                            +"</script>"
                            + "</body>"
                            + "</html>";
                }else if (file.startsWith("/consulta")){

                    String ciudad = file.split("=")[1];
                    outputLine = "HTTP/1.1 200 OK\r\n"
                            + "Content-Type: text/html\r\n"
                            + "\r\n"
                            + "<!DOCTYPE html>"
                            + "<html>"
                            + "<head>"
                            + "<meta charset=\"UTF-8\">"
                            + "<title>Consulta Clima</title>\n"
                            + "</head>"
                            + "<body>"
                            + ClimaApi.consultaClimaCiudad(ciudad)
                            + "</body>"
                            + "</html>";
                }
                else {
                    outputLine = "HTTP/1.1 200 OK\r\n"
                            + "Content-Type: text/html\r\n"
                            + "\r\n"
                            + "<!DOCTYPE html>"
                            + "<html>"
                            + "<head>"
                            + "<meta charset=\"UTF-8\">"
                            + "<title>404</title>\n"
                            + "</head>"
                            + "<body>"
                            + "Página no encontrada"
                            + "</body>"
                            + "</html>";



                }

            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set

    }
}

