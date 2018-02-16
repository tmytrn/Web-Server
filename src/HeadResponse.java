import java.io.OutputStream;

public class HeadResponse extends Response {
  public HeadResponse( Request request, Resource resource, MimeTypes mimeTypes ) {
    super( request, resource, mimeTypes );
    this.setCode( 200 );
    this.setReasonPhrase( "OK" );
  }

  @Override
  void send( OutputStream out ) {

  }
}
