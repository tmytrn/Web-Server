import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

public class PostResponse extends Response {

  public PostResponse(Request request, Resource resource, MimeTypes mimeTypes){
    super(request, resource, mimeTypes);
    this.setCode(200);
    this.setReasonPhrase("OK" );
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
