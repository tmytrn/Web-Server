import java.io.*;
import java.text.SimpleDateFormat;

public class GetResponse extends Response {
  private MimeTypes mimeTypes;

  public GetResponse( Request request, Resource resource, MimeTypes mimeTypes ) {
    super( request, resource );
    this.mimeTypes = mimeTypes;
    this.setCode( 200 );
    this.setReasonPhrase( "OK" );
  }

  private String getMimeType( File file ) {
    String fileName = file.getName();
    String[] type = fileName.split( "\\." );
    return mimeTypes.lookup( type[type.length - 1] );
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

  private void buildResourceHeaders( ) {
    File content = this.getResource().getFile();
    SimpleDateFormat fileDateFormat = new SimpleDateFormat( "EEE, dd MMM yyyy hh:mm:ss " );
    this.getResponseHeaders().put( "Last Modified", fileDateFormat.format( content.lastModified() ) + "GMT" );
    this.getResponseHeaders().put( "Content-Length", String.valueOf( content.length() ) );
    this.getResponseHeaders().put( "Content-Type", getMimeType( content ) + "; charset=utf-8" ); //include charset
  }


}
