import java.io.OutputStream;

public class ServerErrorResponse extends Response {
  public ServerErrorResponse() {
    super();
    this.setCode( 500 );
    this.setReasonPhrase( "Server Error" );
  }

  @Override
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

}
