import java.io.*;
import java.net.*;
import java.util.*;

public class WebServer {

  private static final String HTTPDCONFPATH = "conf/httpd.conf";
  private static final String MIMETYPESPATH = "conf/mime.types";

  private ServerSocket socket;
  private HttpdConf configuration;
  private MimeTypes mimeTypes;

  public WebServer( ) {
    this.configuration = new HttpdConf( HTTPDCONFPATH );
    this.mimeTypes = new MimeTypes( MIMETYPESPATH );
    this.loadConfigurationFiles();
    this.listenToPort();
  }

  public void loadConfigurationFiles( ) {
    this.configuration.load();
    this.mimeTypes.load();
  }

  public void listenToPort( ) {
    Socket client = null;
    int numberOfRequests = 0;
    int portNumber = Integer.parseInt( this.configuration.lookupConfiguration( "Listen" ) );

    try {
      this.socket = new ServerSocket( portNumber );
      while ( true ) {
        client = this.socket.accept();
        System.out.println( "Request Number is: " + ++numberOfRequests );
        Worker worker = new Worker( client, this.configuration, this.mimeTypes );
//        worker.run();
        Thread thread = new Thread( worker, Integer.toString( numberOfRequests ) );
        thread.start();
      }

    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }

  public static void main( String[] args ) {
    WebServer webServer = new WebServer();
//    try {
//      Htpassword htpassword = new Htpassword( "public_html/.htpasswd" );
//      htpassword.isAuthorized( "Authorization: <type> <credentials>" );
//    } catch ( IOException e ) {
//      e.printStackTrace();
//    }
  }
}
