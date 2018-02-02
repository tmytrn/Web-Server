import java.util.*;

public class WebServer {

  public static void main( String[] args ) {
    MimeTypes mimesFile = new MimeTypes( "conf/mime.types" );
    mimesFile.load();
    System.out.println( mimesFile.lookup( "ai" ) );

  }
}
