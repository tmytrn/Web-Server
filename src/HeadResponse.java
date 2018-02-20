import java.io.*;
import java.text.SimpleDateFormat;

public class HeadResponse extends Response {

  private MimeTypes mimeTypes;

  public HeadResponse( Request request, Resource resource, MimeTypes mimeTypes ) {
    super( request, resource );
    this.mimeTypes = mimeTypes;
    this.setCode( 200 );
    this.setReasonPhrase( "OK" );
    this.putResourceHeaders();
  }

  public void send( OutputStream out ) {
    String response = this.createHeaders();
    try {
      out.write( response.getBytes() );
      out.flush();
      out.close();
    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }

  private void putResourceHeaders( ) {
    File content = this.getResource().getFile();
    setContentLength( ( int ) content.length() );

    if ( content != null ) {
      this.getResponseHeaders().put( "Last-Modified", getLastModifiedDate( content ) );
      this.getResponseHeaders().put( "Content-Length", String.valueOf( content.length() ) );
      this.getResponseHeaders().put( "Content-Type", getMimeType( content ) + "; charset=utf-8" );
    }
  }

  private String getLastModifiedDate( File file ) {

    SimpleDateFormat fileDateFormat = new SimpleDateFormat( "EEE, dd MMM yyyy HH:mm:ss z" );
    return fileDateFormat.format( file.lastModified() );

  }

  private String getMimeType( File file ) {

    String fileName = file.getName();
    String[] type = fileName.split( "\\." );

    if ( this.mimeTypes.lookup( type[type.length - 1] ) != null ) {
      return this.mimeTypes.lookup( type[type.length - 1] );
    }

    return "text/text";

  }

}
