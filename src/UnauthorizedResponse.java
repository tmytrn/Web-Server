import java.io.OutputStream;

public class UnauthorizedResponse extends Response{
  public UnauthorizedResponse(Request request, Resource resource){
    super(request, resource);
    this.setCode(401);
    this.setReasonPhrase( "Unauthorized" );
  }

  @Override
  void send( OutputStream out ) {

  }
}

