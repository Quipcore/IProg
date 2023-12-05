package assignment_2.stream_sockets.xml;

import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.xml.sax.*;

import javax.print.Doc;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Socket;



public class XMLReceiver extends Thread{

    private final BufferedReader in;
    private final Socket socket;
    private int exitCode;

    public XMLReceiver(Socket socket) throws IOException {
        this.exitCode = -1;
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        start();

    }

    public XMLReceiver(){
        this.socket = null;
        this.in = null;
        start();
    }


    @Override
    public void run() {

        SAXBuilder builder = new SAXBuilder();

        do{
            try {
                String docURL = "https://atlas.dsv.su.se/~pierre/i/05_ass/ip1/2/2.1.3/message.dtd";
                String msg = in.readLine();
                StringReader characterStream = new StringReader(msg);

                Document document = builder.build(characterStream);

//                String receivedXML = in.readLine();
//                Document document = builder.build(new StringReader(receivedXML));

                Element header = getChildElement(document.getRootElement(),"header");
                Element id = getChildElement(header,"id");
                String name =  getChildElement(id,"name").getContent(0).getValue();
                String email = getChildElement(id,"email").getContent(0).getValue();

                Element body = getChildElement(document.getRootElement(), "body");
                String message = body.getContent(0).getValue();

                System.out.printf("<%s> (<%s>): <%s>\n",name,email,message);
            }
            catch (Exception e){
                closeSocket();
                try {
                    throw e;
                } catch (JDOMException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }while(false);

        System.out.println("Closing receiver");
        exitCode = 0;
    }


    private Element getChildElement(Element element, String name){
        return element.getContent().stream()
                .filter(content -> content instanceof Element)
                .map(content -> (Element)content)
                .filter(elem -> elem.getName().equals(name))
                .findFirst()
                .get();
    }


    private void closeSocket(){
        try {
            if(socket != null){
                socket.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getExitCode(){
        return exitCode;
    }
}
