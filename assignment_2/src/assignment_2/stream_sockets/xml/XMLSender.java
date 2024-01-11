package assignment_2.stream_sockets.xml;


import org.jdom2.Document;

import javax.xml.parsers.DocumentBuilder;
import java.net.Socket;

public class XMLSender extends Thread{


    public XMLSender(Socket socket){
        start();
    }

    public int getExitCode(){
        return -1;
    }

    @Override
    public void run() {
        while(true){
            Document document = new Document();
        }
    }
}
