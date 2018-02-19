import java.io.OutputStream;

public class ForbiddenResponse extends Response{

  public ForbiddenResponse(Request request, Resource resource){
    super(request, resource);
    this.setCode(403);
    this.setReasonPhrase( "Forbidden" );
  }

  @Override
  public void send( OutputStream out ) {
    String response = this.firstHeadersLine();

    try {
      out.write( response.getBytes() );
      out.flush();
      out.close();
    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }

}
