import java.util.StringTokenizer;

public class WebServer {

  public static void main( String[] args ) {
    MimeTypes mimesFile = new MimeTypes( "conf/mime.types" );
    mimesFile.load();

//      StringTokenizer tokens = new StringTokenizer( "application/pdf   pdf" );
//      tokens.nextToken();
//      System.out.println( tokens.nextToken() );
  }
}
