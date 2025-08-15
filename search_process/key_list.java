package search_process;

import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;

public class key_list {
    //////키 리스트를 가지는 클래스
    private ArrayList<key_freqeuncy> key_list;
    public key_list(){
        ArrayList<key_freqeuncy> key_list = new ArrayList<>();
    }
    public ArrayList<key_freqeuncy> get_keyList(){
        return key_list;
    }
    public void setKeyList(ArrayList<key_freqeuncy> keyList){
        this.key_list = keyList;
    }
    public void add_key_at_the_end(String search_key){
        //////검색어 키를 리스트의 마지막에 추가
        key_freqeuncy new_search_key = new key_freqeuncy(search_key);
        key_list.add(new_search_key);
    }
    public void add_key_in_order(String search_key){
        ////검색어를 빈도수 기준으로 알맞은 위치에 추가
        int n = -1;
        for (int i = 0 ; i< key_list.size();i++){
            if (search_key.equals(key_list.get(i).getSearch_key())) {
                /////이미 같은 검색어가 있을 경우 단순히 빈도수를 하나 추가
                key_list.get(i).one_more();
                n = i;

                break;
            }
        }
        System.out.println(n);
        if (n == -1){
            add_key_at_the_end(search_key); ////key가 내부에 존재 안 하는 경우 단순히 리스트 맨 뒤에 추가
        }else{////key가 내부에 존재하는 경우, 순서를 다시 조정해야함
            Boolean swapped = true;
            while (n != 0 && swapped == true){
                if (key_list.get(n).compareTo(key_list.get((n-1))) > 0){
                    Collections.swap(key_list, n-1, n);
                    swapped = true;
                }else{
                    swapped = false;
                }
                n--;
            }
        }
    }
    public String String_topten(){
        //////리스트의 맨 앞에 10개의 원소를 추출, 10개 이하라면 모두 추출
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i< Math.min(10,key_list.size()) ;i++){
            sb.append(key_list.get(i).getSearch_key());
            sb.append("\n");
        }
        String s = sb.toString();
        return s;
    }
}

