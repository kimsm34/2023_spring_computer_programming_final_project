package data_process;

class preUser{
    //////id와 pw를 필드로 가지는 기본적인 유저 클래스
    private String id;
    private String pw;
    public preUser(String id, String pw){
        this.id = id;
        this.pw = pw;
    }
    public String getId() {
        return this.id;
    }

    public String getPw() {
        return this.pw;
    }
}