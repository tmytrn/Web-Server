import java.io.OutputStream;

public class UnauthorizedResponse extends Response{
  public UnauthorizedResponse(Request request, Resource resource, MimeTypes mimeTypes){
    super(request, resource, mimeTypes);
    this.setCode(401);
    this.setReasonPhrase( "Unauthorized" );
  }

  @Override
  public void send( OutputStream out ) {
    String response = this.firstHeadersLine() + getWWWResponseHeader() + "\r\n";
    try {
      out.write( response.getBytes() );
      out.flush();
      out.close();
    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }

  private String getWWWResponseHeader(){
    return "WWW-Authenticate: Basic realm=\"Access to staging site\"";
  }


}

