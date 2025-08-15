package data_process;
import search_process.key_list;

import java.util.ArrayList;

public class Login{
    private User loginMember;
    private ArrayList<User> allmembers;
    private ArrayList<User> leftmembers;
    private ArrayList<String> systemlog;
    private key_list keyList;
    private FileManager filemanager = new FileManager("data/");
    public Login() {
        loginMember = null; ///현재 로그인 한 멤버
        allmembers = filemanager.loadUsersFromFile("userlist.txt"); //////현재 가입된 멤버에 대한 정보를 저장
        leftmembers = filemanager.loadUsersFromFile("leftuserlist.txt"); //////떠난 멤버에 대한 정보를 저장
        systemlog = filemanager.loadSystemLogFromFile(); ///////모든 요청 사항 기록에 대한 정보를 저장
        keyList = new key_list(); ////////검색어를 저장
        keyList.setKeyList(filemanager.loadSearchKeysFromFile());
    }
    public void join(String ID, String pw) throws Exception{
        String id = ID.toLowerCase(); /////가입 할 시 아이디의 경우 대소문자 구분 X -> 소문자로 일괄 변경
        if (idexist(id)) { ////이미 존재하는 아이디인지 확인
            throw new Exception("id already exist");
        }
        if (!validpw(pw)){ ///////유효한 비밀번호인지 확인
            throw new Exception("invalid password");
        }
        User newMember = new User(id, pw);
        allmembers.add(newMember);
        filemanager.saveUsersToFile(allmembers, "userlist.txt");/////파일 업데이트

    }
    public boolean idexist(String id){
        String lowId = id.toLowerCase();
        /////소문자로 변경한 후 겹치는 아이디가 있는지 확인
        for (User m : allmembers) {
            if (m.getId().equals(lowId)) {
                return true;
            }
        }
        return false;
    }
    public boolean validpw(String pw) {
        // Check password length
        if (pw.length() < 4) {
            return false;
        }
        // Check first character is alphabetic
        if (!Character.isLetter(pw.charAt(0))) {
            return false;
        }
        // Check for valid characters (@ and % allowed)
        for (char c : pw.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != '@' && c != '%') {
                return false;
            }
        }
        return true;
    }

    public void login(String id, String pw) throws Exception {
        /////매칭되는 아이디와 패스워드가 있다면 그 유저를 로그인한 유저로 설정
        for (User m : allmembers) {
            if (m.getId().equals(id) && m.getPw().equals(pw) ) {
                loginMember = m;
                return;
            }
        }
        throw new Exception("no matching id & pw; login failed");
    }

    public void logout(String id) throws Exception{
        //아이디가 현재 로그인 한 멤버의 것이라면 로그인한 멤버 변수를 null로 변경
        if (loginMember.getId().equals(id)){
            loginMember = null;
        }else{
            throw new Exception("no matching id, logout failed");
        }
    }
    public void leave(String id, String pw) throws Exception{
        //////매칭되는 id/pw에 대하여 그 유저를 회원 유저리스트에서 삭제하고 탈퇴 리스트에 추가
        for (User m : allmembers) {
            if (m.getId().equals(id) && m.getPw().equals(pw) ) {
                allmembers.remove(m);
                leftmembers.add(m);
                filemanager.saveUsersToFile(allmembers, "userlist.txt");
                filemanager.saveUsersToFile(leftmembers,"leftuserlist.txt");
                return;
            }
        }throw new Exception("no matching user to this id and password");

    }
    public void recover(String id, String pw) throws Exception{
        ////탈퇴와 반대로 진행
        for (User m : leftmembers) {
            if (m.getId().equals(id) && m.getPw().equals(pw)) {
                leftmembers.remove(m);
                allmembers.add(m);
                filemanager.saveUsersToFile(allmembers, "userlist.txt");
                filemanager.saveUsersToFile(leftmembers,"leftuserlist.txt");
                return;
            }
        }throw new Exception("no matching user to this id and password");
    }
    public String admin_load_acc() throws Exception{
        ///////현재 회원 멤버리스트에 접근하여 회원 아이디를 순차적으로 프린트.
        if (loginMember.getId().equals("admin")){
            StringBuilder sb = new StringBuilder();
            for (User user : allmembers) {
                sb.append(user.getId());
                sb.append("\n");
            }
            return sb.toString();
        }else{
            throw new Exception("login with admin acc to load acc list");
        }
    }
    public void add_log(String log, String id){
        /////요청 정보를 로그 리스트에 추가.
        String s = "[" + id + "] " + log;
        systemlog.add(s);
        filemanager.addSystemLogToFile(s);
    }
    public String admin_load_log() throws Exception{
        ///////로그 리스트에 접근하여 한 줄씩 프린트.
        if (loginMember.getId().equals("admin")){
            StringBuilder sb = new StringBuilder();
            for (String log : systemlog) {
                sb.append(log);
                sb.append("\n");
            }
            return sb.toString();
        }else{
            throw new Exception("login with admin acc to load log");
        }
    }
    public User getLoginMember(){
        return loginMember;
    }
    public void addSearchkeyToLoginMember(String key) throws Exception{
        //////검색어 정보를 현재 로그인한 유저의 검색어 리스트 필드에 추가.
        if (loginMember != null){
            loginMember.addSearchKey(key);
            filemanager.saveUsersToFile(allmembers,"userlist.txt");
        }else{
            throw new Exception("no logged in member");
        }
    }
    public ArrayList<String> getSearchKeyOfLoginMember(){
        return loginMember.getSearchList();
    }
    //////로그인 한 유저의 검색어 리스트에 접근, 리턴
    public ArrayList<String> getSearchKeyofFriend(String id) throws Exception{
        ////////id가 매칭되는 유저의 검색어리스트 필드에 접근, 리턴
        for (User m:allmembers){
            if (m.getId().equals(id)){
                return m.getSearchList();
            }
        }throw new Exception("friend with this id doesn't exist");
    }
    public void add_search_key(String search_key){
        //////로그인 한 유저의 검색어 리스트에 검색어를 추가
        keyList.add_key_in_order(search_key);
        filemanager.saveKeysToFile(keyList);
    }
    public String getTopTenSearchKeys(){
        return keyList.String_topten();
    }
}
