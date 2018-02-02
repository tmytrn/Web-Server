import java.util.*;

public class WebServer {

  public static void main( String[] args ) {
    MimeTypes mimesFile = new MimeTypes( "conf/mime.types" );
    mimesFile.load();
    HttpdConf configuration = new HttpdConf( "conf/httpd.conf" );
    configuration.load();
    System.out.println(configuration.lookup("Listen"));
    //System.out.println( mimesFile.lookup( "mp3" ) );

  }
}
