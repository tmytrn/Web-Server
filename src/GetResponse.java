import java.io.*;
import java.text.SimpleDateFormat;

public class GetResponse extends Response {

  public GetResponse( Request request, Resource resource, MimeTypes mimeTypes ) {
    super( request, resource, mimeTypes );
    if(this.getRequest().getHeaders().containsKey( "If-Modified-Since" )){

    }

    this.setCode( 200 );
    this.setReasonPhrase( "OK" );
  }

  public void send( OutputStream out ) {
    String response = this.createHeaders();
    try {
      String res = new String( response.getBytes() );
      System.out.println( res );
      out.write( response.getBytes() );
      out.flush();
      sendResource( out );
      out.flush();
      out.close();
    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }

  private void sendResource( OutputStream out ) { // made into buffered output stream
    File file = this.getResource().getFile();
    byte[] fileBytes = new byte[( int ) file.length()];
    try {
      FileInputStream fileToArray = new FileInputStream( file );
      fileToArray.read( fileBytes );
      out.write( fileBytes );
    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }



}
