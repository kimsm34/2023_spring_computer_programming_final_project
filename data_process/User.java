package data_process;

import java.util.ArrayList;

public class User extends preUser{
    ////preUser에 검색어리스트 필드를 추가한 유저 타입.
    private ArrayList<String> searchList;

    public User(String id, String pw){
        super(id.toLowerCase(),pw);
        this.searchList = new ArrayList<>();
    }
    public ArrayList<String> getSearchList() {
        return searchList;
    }
    public void addSearchKey(String key){
        ////본 메소드를 이용하여 검색어를 추가 가능
        searchList.add(key);
    }
}
