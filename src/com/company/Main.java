package com.company;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import org.w3c.dom.ls.LSOutput;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Main {

    //TESTIT tänne
    // testataan GET-funktiosta: tuleeko poikkeus, jos käyttäjä antaa virheellisen URLin
    // jos ei mene läpi, palautettava virheviesti käyttäjälle

    public static boolean testGetShouldThrowException() {
        try {
            get("roskaa");
            return false;
        } catch (Exception e) {
            return true;
        }
    }



    public static boolean testShouldReturn200 () {

        try {
            int response = get("http://www.google.com");

            if (response == 200) {

                return true;
            } else {
                return false;

            }
        } catch (Exception e) {
            return false;


        }
    }


    private static final String USAGE = "Instructions: the program connects to a given address and returns server information";

    private final static String HELP = "help = list of commands\nexit = exits the program\nget [URL] = connects to the given URL and returns server information";

    public static void main(String[] args) {

        if(!testGetShouldThrowException()) {
            System.out.println("ShouldThrowException test FAILED");
            System.exit(-1);
        }

        if (!testShouldReturn200()) {
            System.out.println("Return200 test failed");
            System.exit(-1);
        }

        if (args.length > 0) {
            //if there are arguments given
            parseArgs(args);

        } else { //if no args

            mainLoop();

        }
    }

    static void mainLoop() {

        while (true) {
            Scanner lukija = new Scanner(System.in);

            String komento = lukija.nextLine();

            String [] komennot = komento.split(" ");
            //splitataan komento foo ba r välilyönnillä : ["foo, "bar"] 0-indeksissö foo, 1. bar
            // eli ["get", "something"]

            switch (komennot[0]) {
                case "exit":
                    return;

                case "help":
                    System.out.println(HELP);
                    break;

                case "get":
                    try {
                        get(komennot[1]);
                    } catch (Exception e) {
                        System.out.println("Sorry, invalid URL");
                        e.printStackTrace();
                    }
                    break;
                case "post":
                    try {
                        post(komennot);
                    } catch (Exception e) {
                        System.out.println("Sorry, invalid URL");
                    }
                    }

            }
        }


    static void parseArgs(String[] args) {
        switch (args[0]) {
            case "--help":
            case "-h":
                printUsage();
                break;

            case "get":
                try {
                    get(args[1]);
                } catch (Exception e) {
                    System.out.println("Sorry, invalid URL");
                }
                break;

            case "post":
                try {
                    post(args);

                } catch (Exception e) {
                    System.out.println("Sorry, invalid URL");
                }
        }
    }
/*int arr = {args.length);
    int start = 3, end = args.length-1;
    int[] slice = Arrays.copyOfRange(arr, start, end + 1);
                try {
        sendPost(args[1], args[2], args[]);


    } catch (Exception e) {
        System.out.println("Sorry");
    }*/

    static void printUsage() {
        System.out.println(USAGE);
    }


    static int get(String requestUrl) throws Exception {

        URL url = new URL(requestUrl); //esim 10.11.1.1/users

        HttpURLConnection con = (HttpURLConnection)url.openConnection();

        con.setRequestMethod("GET");
        // con.setRequestProperty("User-Agent", "Moi"); //USer agent on header, kertoo palvelimelle mikä sovellus kysyy

        int responseCode = con.getResponseCode(); // aletaan lukemaan vastausta, nyt java tajuaa että pitää lähettää requesti

        System.out.println("STATUS CODE: " + responseCode);

        for (Map.Entry<String, List<String>> entries : con.getHeaderFields().entrySet()) {
            String values = " ";
            if (entries.getKey() == null) { //ks tehtävänanto, ei tarvii null = jne tapausta tulostaa
                continue;
            }
            for (String value : entries.getValue()) {

                values += value + " ";
            }

            System.out.println(entries.getKey() + ": " + values);


        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String helpie = response.toString();

        int length = helpie.length();

        //print result
        int l;

        if (length <= 100) {
            l = length;
        } else {
            l = 100;
        }

        System.out.println("Length of body: " + length + " characters\n" + helpie.substring(0, l));

        return responseCode;



    }
   /// POST-osio

    static void post(String [] komennot)  throws Exception {

        URL obj = new URL(komennot[1]);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        //add request header
        con.setRequestMethod("POST");
        // Send post request
        int i = 3; // kolmannesta komennosta eteenpäin otetaan huomioon kaikki headerit
        while (i < komennot.length) {
            String[] jaettuString = komennot[i].split(":");
            con.setRequestProperty(jaettuString[0], jaettuString[1]);
            i++;
        }
        String urlData = (komennot[2]);
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlData);
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        System.out.println("STATUS: " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //print result
        System.out.println(response.toString());

    }

//YRITYS
    /* private void header(String header) throws Exception {

    }

    private void data(String data) throws Exception {

    }

    static void sendPost(String url, String data, String [] headers) throws Exception {
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection)obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
       // con.setRequestProperty("User-Agent", USER_AGENT);

        //lisää koko taulukko headereiksi loopilla:
        int helps = 0;

       // String [] tableheaders = new String [headers.length-1];
        ArrayList listheaders = new ArrayList;

        while (helps < headers.length) {
            helps++;


        con.setRequestProperty(listheaders);

        String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString()); */



    }

