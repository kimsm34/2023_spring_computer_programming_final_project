package search_process;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import core_process.SimpleHttpClient;


public class Search {
    private SimpleHttpClient simpleHttpClient;
    public Search(){
        simpleHttpClient = new SimpleHttpClient();
    }
    public String neededDataFromSearch(String arg){
        StringBuilder sb = new StringBuilder();
        //////구글 API에서 얻어온 JSON 형식 정보를 response에 저장
        String response =  simpleHttpClient.customSearch(arg);

        /////json 외부 라이브러리를 통해 response를 object화 하고, 필요한 정보를 추출
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(response);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject queries = jsonObject.get("queries").getAsJsonObject();
        JsonArray request = queries.get("request").getAsJsonArray();
        JsonObject requestZero = request.get(0).getAsJsonObject();
        String totalResults = requestZero.get("totalResults").getAsString();
        String searchTerms = requestZero.get("searchTerms").getAsString();

        /////추출한 정보를 모아 string으로 변환, 리턴
        sb.append(searchTerms);
        sb.append("\n");
        sb.append(totalResults);
        sb.append("\n");
        JsonArray items = jsonObject.get("items").getAsJsonArray();
        for(int i = 0; i<3; i++) {
            sb.append(items.get(i).getAsJsonObject().get("title").getAsString());
            sb.append(" : ");
            sb.append(items.get(i).getAsJsonObject().get("link").getAsString());
            sb.append("\n");
        }
        String s = sb.toString();

        return s;
    }

}