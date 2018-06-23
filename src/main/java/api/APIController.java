package api;

import java.io.*;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;

@RestController
public class APIController {

    @RequestMapping(method = RequestMethod.GET)
    public String greeting() {
        return "No stalking!";
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST)
    public Object getHTTPBody(@RequestBody String request) {
        JSONObject json;
        String action, data;
        ProcessBuilder builder;
        Process process;
        InputStream stream;
        Scanner scanner;
        String output;
        try {
            json = new JSONObject(request);
            action = json.getString("Action");
            data = json.getString("Data");
            /* Just for testing purposes.
            Gson gson = new Gson();
            String jsonstring = gson.toJson(request);
            */
        } catch (JSONException e) {
            return "JSONException: " + e.getMessage();
        }
        try {
            switch (action) {
                case "sudoCommand":
                    builder = new ProcessBuilder();
                    builder.command("sh", "-c", "echo somepassword | sudo -S " + data);
                    builder.directory(new File(System.getProperty("user.home")));
                    process = builder.start();
                    stream = process.getInputStream();
                    scanner = new Scanner(stream).useDelimiter("\\A");
                    output = scanner.hasNext() ? scanner.next() : "";
                    System.out.println(output);
                    return output;
                case "normalCommand":
                    builder = new ProcessBuilder();
                    builder.command("sh", "-c", data);
                    builder.directory(new File(System.getProperty("user.home")));
                    process = builder.start();
                    stream = process.getInputStream();
                    scanner = new Scanner(stream).useDelimiter("\\A");
                    output = scanner.hasNext() ? scanner.next() : "";
                    System.out.println(output);
                    return output;
                default:
                    System.out.println("This is error");
                    return "Invalid Action: " + action;
            }
        } catch (JSONException e) {
            return "JSONException: " + e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}