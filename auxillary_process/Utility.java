package auxillary_process;

import java.util.ArrayList;

public class Utility {
    public static String ArrayListToStringWithLineBreaking(ArrayList<String> list){
        //////어레이리스트의 원소를 한 줄 씩 띄어서 String으로 저장
        StringBuilder sb = new StringBuilder();
        for (String s:list){
            sb.append(s + "\n");
        }
        String result = sb.toString();
        return result;
    }
}
