import java.io.OutputStream;

public class ServerErrorResponse extends Response{
  public ServerErrorResponse(Request request, Resource resource, MimeTypes mimeTypes){
    super(request, resource, mimeTypes);
    this.setCode(500);
    this.setReasonPhrase("Server Error" );
  }

  @Override
  public void send( OutputStream out ) {

  }
}
