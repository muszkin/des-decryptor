package Des;
/**
 * Created by muszkin on 02.10.17.
 */
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.Base64;
import java.util.Scanner;


public class Des {

    public static void main(String args[]){
        if (args.length < 5){
            System.out.println("Too few arguments");
            System.exit(0);
        }
        String srcFile = args[0];
        String dstDir = args[1];
        String key = args[2];
        String iterations = args[3];
        String fileToGrab = args[4];
        try {
            Scanner in = new Scanner(new FileReader(srcFile));
            StringBuilder sb = new StringBuilder();
            while(in.hasNext()){
                sb.append(in.next());
            }
            in.close();
            String data = sb.toString();
            JSONArray jsonData = new JSONArray(data);
            for (int i=0; i < jsonData.length();i++){
                JSONObject jsonObject = jsonData.getJSONObject(i);
                String ftp_host = jsonObject.getString("ftp_host");
                String ftp_login = jsonObject.getString("ftp_login");
                String ftp_pass = jsonObject.getString("ftp_pass");
                String ftp_directory = jsonObject.getString("ftp_directory");
                int ftp_port = jsonObject.getInt("port");
                String id = jsonObject.getString("id");

                byte[] ftp_password = Base64.getDecoder().decode(ftp_pass.getBytes());
                ftp_pass = new String(DesEncrypter.getInstatnce(key, Integer.parseInt(iterations)).decrypt(ftp_password));

                connect(ftp_host,ftp_login,ftp_pass,ftp_port,ftp_directory,id,dstDir,fileToGrab);

            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void connect(String ftp_host, String ftp_login, String ftp_pass, int ftp_port, String ftp_directory, String id, String dstDir,String fileToGrab){
        try{
            System.out.println("Trying to connect to "+ ftp_host + " with login " + ftp_login + " and password "+ ftp_pass);
            FTPClient ftpClient = new FTPClient();
            ftpClient.setDefaultTimeout(2000);
            ftpClient.setConnectTimeout(2000);
            ftpClient.setDataTimeout(2000);
            ftpClient.connect(ftp_host,ftp_port);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
            }
            boolean success = ftpClient.login(ftp_login,ftp_pass);;
            if (!success) {
                System.out.println("Could not login to the server");
                return;
            } else {
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                System.out.println("logged in");
                boolean changedirSuccess = ftpClient.changeWorkingDirectory(ftp_directory);
                if (changedirSuccess){
                    System.out.println("dir changed to " + ftp_directory);
                }else{
                    System.out.println("dir not changed to" +  ftp_directory);
                }
                String remoteFile = fileToGrab;
                File downloadFile = new File(dstDir + "/" + id + "-htaccess.txt");
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
                boolean downloadSuccess = ftpClient.retrieveFile(remoteFile,outputStream);
                outputStream.close();
                if (downloadSuccess) {
                    System.out.println(remoteFile + " has been downloaded successfully.");
                }else{
                    System.out.println(remoteFile + " has not been downloaded successfully.");
                }
            }
        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }

    }
}
