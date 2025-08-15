package core_process;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class SimpleHttpClient {
    public String customSearch(String key) {

        String API_KEY = "AIzaSyCWhDaSrElkOl5A08n5efrWMn4l6rhGONA";
        String ENGINE_ID = "9345111b8ffe447dd";
        String url = "https://www.googleapis.com/customsearch/v1?key="+ API_KEY +"&cx=" + ENGINE_ID +"&q=" + key;
        System.out.println(url);

        try {
            // Create a URL object from the specified URL
            URL requestUrl = new URL(url);
            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            // Set the request method (GET by default)
            connection.setRequestMethod("GET");
            // Send the request and receive the response
            int responseCode = connection.getResponseCode();
            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Print the response
            System.out.println("Response Code: " + responseCode);
            System.out.println("Response Body:");
            // Close the connection
            connection.disconnect();
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}