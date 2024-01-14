package net.examclient;

import com.jamesmurty.utils.XMLBuilder;
import com.jamesmurty.utils.XMLBuilder2;

public class ProtocolBuilder {

    XMLBuilder2 xmlBuilder;

    private static final String XML_HEADER =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE message>";

    public ProtocolBuilder(String name){
        xmlBuilder = XMLBuilder2.create(name);
    }

    public ProtocolBuilder addHeader(String username, String email,String command){
        xmlBuilder.element("header")
                .element("protocol")
                    .element("type").text("").up()
                    .element("version").text("1.0").up()
                    .element("command").text(command).up()
                .up()
                .element("id")
                    .element("username").text(username).up()
                    .element("email").text(email).up()
                .up()
                .up();
        return this;
    }

    public ProtocolBuilder addBody(){
        //                .element("java-xmlbuilder")
//                    .attribute("language", "Java")
//                    .attribute("scm","SVN")
//                    .element("Location")
//                        .attribute("type", "URL")
//                        .text("http://code.google.com/p/java-xmlbuilder/")
//                        .up()
//                    .up()
//                .element("JetS3t")
//                    .attribute("language", "Java")
//                    .attribute("scm","CVS")
//                    .element("Location")
//                    .attribute("type", "URL")
//                    .text("http://jets3t.s3.amazonaws.com/index.html");
        xmlBuilder.element("body").up();
        return this;
    }

    @Override
    public String toString() {
        return XML_HEADER + xmlBuilder.asString();
    }
}
