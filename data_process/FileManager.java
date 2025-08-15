package data_process;

import data_process.User;
import search_process.key_freqeuncy;
import search_process.key_list;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.StandardOpenOption;
import java.nio.file.FileSystems;
public class FileManager {
    private String filePath;
    public FileManager(String filePath) {
        this.filePath = filePath;
    }

    public void saveKeysToFile(key_list keyList){
        /////검색어를 저장하는 파일을 업데이트
        try {
            Path logFilePath = FileSystems.getDefault().getPath(filePath, "keylist.txt");
            Files.write(logFilePath, new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
            for (key_freqeuncy key : keyList.get_keyList()){
                Files.write(logFilePath, ((key.getSearch_key() + "," + key.getFreq())+ System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            }
            System.out.println("key added to the file.");
        } catch (IOException e) {
            System.out.println("Failed to add key to file.");
            e.printStackTrace();
        }
    }
    public ArrayList<key_freqeuncy> loadSearchKeysFromFile() {
        /////검색어 저장 파일에서 정보를 로드하여 어레이리스트로 저장, 리턴
        ArrayList<key_freqeuncy> key_list = new ArrayList<>();
        try {
            Path userFilePath = FileSystems.getDefault().getPath(filePath, "keylist.txt");
            List<String> userLines = Files.readAllLines(userFilePath);
            String line;
            for (int i = 0; i < userLines.size(); i++) {
                line = userLines.get(i);
                String[] keyData = line.split(",");
                String search_key = keyData[0];
                int freq = Integer.parseInt(keyData[1]);
                key_freqeuncy key_freqeuncy = new key_freqeuncy(search_key, freq);
                key_list.add(key_freqeuncy);
            }
        } catch (IOException e) {
            System.out.println("Failed to load user data from file.");
            e.printStackTrace();
        }
        return key_list;
    }
    public void saveUsersToFile(ArrayList<User> allmembers, String filename) {
        /////유저정보의 필드를 줄 바꿈과 comma를 기준으로 구분하며 파일에 쓰기
        ////현재 회원, 탈퇴 회원 두 가지 파일을 다룰 것이므로 파일 이름도 인풋으로 받음
        try {
            Path logFilePath = FileSystems.getDefault().getPath(filePath, filename);
            Files.write(logFilePath, new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
            for (User user : allmembers){
                StringBuilder sb = new StringBuilder();
                sb.append(user.getId() + "," + user.getPw());
                for (String key : user.getSearchList()){
                    sb.append("," + key);
                }
                String userLine = sb.toString();
                Files.write(logFilePath, (userLine + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            }
            System.out.println("Sywstem log added to the file.");
        } catch (IOException e) {
            System.out.println("Failed to add system log to file.");
            e.printStackTrace();
        }
    }
    public ArrayList<User> loadUsersFromFile(String filename) {
        /////회원 정보를 담고 있는 파일에서 정보를 로드하여 어레이 리스트로 저장, 리턴
        ArrayList<User> allmembers = new ArrayList<>();
        try {
            Path userFilePath = FileSystems.getDefault().getPath(filePath, filename);
            List<String> userLines = Files.readAllLines(userFilePath);
            String line;
            for (int i = 0; i < userLines.size(); i++) {
                line = userLines.get(i);
                String[] userData = line.split(",");
                String id = userData[0];
                String pw = userData[1];
                User user = new User(id, pw);
                for (int j = 2; j< userData.length; j++){
                    user.addSearchKey(userData[j]);
                }
                allmembers.add(user);
            }
        } catch (IOException e) {
            System.out.println("Failed to load user data from file.");
            e.printStackTrace();
        }
        return allmembers;
    }
    public ArrayList<String> loadSystemLogFromFile() {
        ////시스템 로그를 파일에서 읽어와 어레이리스트로 저장, 리턴
        try {
            Path logFilePath = FileSystems.getDefault().getPath(filePath, "systemlog.txt");
            List<String> logLines = Files.readAllLines(logFilePath);
            return new ArrayList<>(logLines);
        } catch (IOException e) {
            System.out.println("Failed to load system log from file.");
            return new ArrayList<>();
        }
    }
    public void addSystemLogToFile(String systemlog) {
        //////시스템 정보를 파일에 쓰기(한 줄씩)
        try {
            Path logFilePath = FileSystems.getDefault().getPath(filePath, "systemlog.txt");
            Files.write(logFilePath, (systemlog + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            System.out.println("System log added to the file.");
        } catch (IOException e) {
            System.out.println("Failed to add system log to file.");
            e.printStackTrace();
        }
    }

}
