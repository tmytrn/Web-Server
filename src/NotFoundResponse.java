import java.io.OutputStream;

public class NotFoundResponse extends Response {
  public NotFoundResponse( Request request, Resource resource ) {
    super( request, resource );
    this.setCode( 404 );
    this.setReasonPhrase( "Not Found" );
  }

  @Override
  void send( OutputStream out ) {

  }

}
