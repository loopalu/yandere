package hello;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import static com.sun.javafx.font.PrismFontFactory.isWindows;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(method = RequestMethod.GET)
    public String greeting() {
        return "No stalking!";
    }

    /*
    @RequestMapping(method = RequestMethod.POST, value = "/api")
    */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST)
    public String getHTTPBody(@RequestBody String request) {
        JSONObject data;
        String action;
        try {
            data = new JSONObject(request);
            action = data.getString("Action");
            System.out.println(action);
        } catch (JSONException e) {
            return "JSONException: " + e.getMessage();
        }

        try {
            switch (action) {
                case "ls":
                    ProcessBuilder builder = new ProcessBuilder();
                    if (isWindows) {
                        builder.command("cmd.exe", "/c", "dir");
                    } else {
                        builder.command("sh", "-c", "ls");
                    }
                    builder.directory(new File(System.getProperty("user.home")));
                    Process process = builder.start();
                    StreamGobbler streamGobbler =
                            new StreamGobbler(process.getInputStream(), System.out::println);
                    Executors.newSingleThreadExecutor().submit(streamGobbler);
                    int exitCode = process.waitFor();
                    assert exitCode == 0;
                    return streamGobbler.toString();
                default:
                    return "Invalid Action: " + action;
            }
        } catch (JSONException e) {
            return "JSONException: " + e.getMessage();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}