package core_process;
import data_process.Login;
import search_process.Search;
import auxillary_process.Utility;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SimpleHttpServer {
    private static Login login = new Login(); ////login 클래스 오브젝트 생성
    private static Search search = new Search(); ////search 클래스 오브젝트 생성
    public static int port = 8080;
    public static void main(String[] args) {
         // If the 8080 port isn't available, try to use another port number.
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                // Create a new thread to handle the client request
                Thread thread = new Thread(new ClientHandler(clientSocket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void process_req(String originalCommand, PrintWriter out) throws Exception{ ///입력받은 path를 이용해 할 일을 하고 out에 전달
        /////클라이언트가 제공한 요청을 처리하여 어떤 response를 내놓을 지 결정하는 함수
        String content = "";
        String[] path = originalCommand.split("%20\\?");///only when it includes " ?", it separates the option part.
        System.out.println(path[0]);
        String options = ""; ////검색 옵션이 있을 경우를 대비하여 option string 생성
        if (path.length > 1) {
            String[] option = path[1].split("&user=");
            options = option[0]; ////option이 있을 때 option string에 따로 저장
        }
        String[] parts = path[0].split("[/?&]");  // Split by "/", "?", and "&"
        String type1 = parts[1]; ///"user" 혹은 "data"
        String type2 = parts[2];  // Access the second element
        String url = "http://localhost:"+ port + originalCommand;
        String id ="";
        String pw ="";
        try{
            switch (type1) {
                case "user": //////계정 관리와 관련한 요청
                    String[] id_pre = parts[3].split("=");
                    String[] pw_pre = parts[4].split("=");
                    ///id와 pw 얻음
                    if (id_pre.length == 1) {
                        id = "";
                    } else {
                        id = id_pre[1];
                    }
                    if (pw_pre.length == 1) {
                        pw = "";
                    } else {
                        pw = pw_pre[1];
                    }

                    switch (type2) {
                        case "join": ////회원가입의 경우를 처리
                            login.join(id, pw);
                            content = "successfully signed up";
                            break;
                        case "login": /////로그인의 경우
                            login.login(id, pw);
                            content = "login was successful!";
                            break;
                        case "logout": ////로그아웃의 경우
                            login.logout(id);
                            content = "logout successfully!";
                            break;
                        case "leave": /////탈퇴의 경우
                            login.leave(id, pw);
                            content = "hope we can see you again~";
                            break;
                        case "recover": /////회원 복구의 경우
                            login.recover(id, pw);
                            content = "glad you're back!";
                            break;
                        default:
                            throw new Exception("invalid access");
                    }
                    break;

                case "data": ///////search와 관련한 요청
                    String[] get_user = originalCommand.split("&user=");
                    id = get_user[1];
                    String[] get_search_key = get_user[0].split("\\?q=");
                    String search_key = "";
                    if (get_search_key.length > 1) {
                        search_key = get_search_key[1];
                    }
                    switch (type2) {
                        case "search":///검색 기능 요청
                            String[] key_pre = parts[3].split("=");
                            String key;
                            if (key_pre.length == 1) {
                                key = "";
                            } else {
                                key = key_pre[1];
                            }
                            content = search.neededDataFromSearch(key + "&" + options);
                            System.out.println(content);
                            break;
                        case "save_data": ////검색어 저장
                            login.addSearchkeyToLoginMember(search_key);
                            login.add_search_key(search_key);
                            break;
                        case "load_data": ///저장 검색어 불러오기
                            ArrayList<String> list = login.getSearchKeyOfLoginMember();
                            content = Utility.ArrayListToStringWithLineBreaking(list);
                            break;
                        case "load_fri": /////친구의 검색어 불러오기
                            String[] fri_pre = parts[3].split("=");
                            String friendId;
                            if (fri_pre.length == 1) {
                                friendId = "";
                            } else {
                                friendId = fri_pre[1];
                            }
                            content = Utility.ArrayListToStringWithLineBreaking(login.getSearchKeyofFriend(friendId));
                            break;
                        case "load_hot": /////top 10 저장 검색어 불러오기
                            content = login.getTopTenSearchKeys();
                            break;
                        case "load_acc": /////admin으로 로그인 된 상태에서 모든 계정 불러오기
                            content = login.admin_load_acc();
                            break;
                        case "load_log": /////admin으로 로그인 된 상태에서 모든 요청 기록 불러오기
                            content = login.admin_load_log();
                            break;
                        default:
                            throw new Exception("invalid access");
                    }
                    break;
            }
            /////아무런 예외 발생 없이 요청이 처리되었을 경우 아래 코드를 수행
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/plain");  // application/json would be OK!
            out.println("Content-Length: " + content.length());
            out.println();
            out.println(content);

        }catch (Exception e){
            //////예외가 발생했을 경우 요청 코드 201로 설정하고, 관련한 메시지를 출력함
            System.out.println("error");
            out.println("HTTP/1.1 201 " + e.getMessage());
            out.println();
        }finally {
            ////예외가 발생했든, 하지 않았든 요청 사항을 기록함
            login.add_log(url,id); /// 로그 추가
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String request = in.readLine();
                System.out.println("Received request: " + request);

                // Extract the path from the request
                String[] requestParts = request.split(" ");
                String path = requestParts[1];
                System.out.println(path);

                // Read the HTML file and send it as the response
                if (path.equals("/")) {  // CASE 1: It sends index.html page to the client.
                    String filePath = "src/index.html";  // Default file to serve
                    File file = new File(filePath);  // Replace with the actual path to your HTML files
                    if (file.exists() && file.isFile()) {
                        System.out.println(file.getAbsolutePath());
                        String contentType = Files.probeContentType(Paths.get(filePath));
                        String content = new String(Files.readAllBytes(Paths.get(filePath)));

                        out.println("HTTP/1.1 200 OK");
                        out.println("Content-Type: " + contentType);
                        out.println("Content-Length: " + content.length());
                        out.println();
                        out.println(content);
                    } else {
                        // File not found
                        out.println("HTTP/1.1 404 Not Found");
                        out.println();
                    }
                } else {  // CASE 2: It sends a specific data to the client as an HTTP Response.
                    // ########################################################
                    ////위에서 정의한 process_req 함수를 이용하여 요청 path를 처리
                    process_req(path, out);
                    // This #AREA# needs to be revised using an external class.

                }
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e){

            }
        }
    }
}
