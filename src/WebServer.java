import java.io.*;
import java.net.*;
import java.util.*;

public class WebServer {

  public static final int DEFAULTPORT = 8096;

  public static void main( String[] args ) {
    MimeTypes mimesFile = new MimeTypes( "conf/mime.types" );
    mimesFile.load();
    HttpdConf configuration = new HttpdConf( "conf/httpd.conf" );
    configuration.load();

//    System.out.println(configuration.lookup("/ab/"));
    System.out.println(configuration.lookup("/"));

    //    //    configuration.load();
    //    //    System.out.println(configuration.lookup("Listen"));
    //    //    //System.out.println( mimesFile.lookup( "mp3" ) );

    Request getRequest = new Request( "GET / HTTP/1.1\n" +
        "Transfer-Encoding: chunked\n" +
        "Date: Sat, 28 Nov 2009 04:36:25 GMT\n" +
        "Server: LiteSpeed\n" +
        "Connection: close\n" +
        "X-Powered-By: W3 Total Cache/0.8\n" +
        "Pragma: public\n" +
        "Expires: Sat, 28 Nov 2009 05:36:25 GMT\n" +
        "Etag: \"pub1259380237;gz\"\n" +
        "Cache-Control: max-age=3600, public\n" +
        "Content-Type: text/html; charset=UTF-8\n" +
        "Last-Modified: Sat, 28 Nov 2009 03:50:37 GMT\n" +
        "X-Pingback: http://net.tutsplus.com/xmlrpc.php\n" +
        "Content-Encoding: gzip\n" +
        "Vary: Accept-Encoding, Cookie, User-Agent\n" );

    Resource resource = new Resource( "/", configuration );

//    System.out.print( "methods\n" );
//    System.out.print( "headers\n" );
//    System.out.print("\r\n" );
//    System.out.print( "body\n" );
//    System.out.print("\r\n" );

    Socket client = null;
    int numberOfRequests = 0;

    try {
      ServerSocket serverSocket = new ServerSocket( DEFAULTPORT );

      while(true){
        client = serverSocket.accept();
        Request request = new Request( client.getInputStream() );
        System.out.println( ++numberOfRequests );
        client.close();
      }

    }
    catch (Exception e){
      //File document = new File(configuration.lookup("DirectoryIndex"));
      //e.printStackTrace();
      //404 error
    }


  }
}
