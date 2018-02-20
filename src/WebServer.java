import configurationreader.HttpdConf;
import configurationreader.MimeTypes;
import threading.Worker;

import java.net.*;

public class WebServer {

  private static final String HTTPD_CONF_PATH = "conf/httpd.conf";
  private static final String MIME_TYPES_PATH = "conf/mime.types";
  private static final int DEFAULT_PORT = 8080;

  private ServerSocket socket;
  private HttpdConf configuration;
  private MimeTypes mimeTypes;

  public WebServer( ) {

    this.configuration = new HttpdConf( HTTPD_CONF_PATH );
    this.mimeTypes = new MimeTypes( MIME_TYPES_PATH );
    this.loadConfigurationFiles();

  }

  public void loadConfigurationFiles( ) {

    this.configuration.load();
    this.mimeTypes.load();

  }

  public void start( ) {

    this.listenToPort();

  }

  public void listenToPort( ) {

    Socket client = null;
    int numberOfRequests = 0;

    try {
      this.socket = new ServerSocket( getPortNumber() );
      while ( true ) {
        client = this.socket.accept();
        Worker worker = new Worker( client, this.configuration, this.mimeTypes );
        Thread thread = new Thread( worker, Integer.toString( ++numberOfRequests ) );
        thread.start();

      }

    } catch ( Exception e ) {
      e.printStackTrace();
    }

  }

  private int getPortNumber( ) {

    if ( this.configuration.lookupConfiguration( "Listen" ) != null ) {
      return Integer.parseInt( this.configuration.lookupConfiguration( "Listen" ) );
    }

    return DEFAULT_PORT;

  }

  public static void main( String[] args ) {

    WebServer webServer = new WebServer();
    webServer.start();

  }

}
