import java.io.OutputStream;

public class ForbiddenResponse extends Response{
  public ForbiddenResponse(Request request, Resource resource){
    super(request, resource);
    this.setCode(403);
    this.setReasonPhrase( "Forbidden" );
  }

  @Override
  void send( OutputStream out ) {

  }
}
