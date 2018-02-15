
public class ServerErrorResponse extends Response{
  public ServerErrorResponse(Request request, Resource resource){
    super(request, resource);
    this.setCode(500);
    this.setReasonPhrase("Server Error" );
  }
}
