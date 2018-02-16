import java.io.OutputStream;

public class BadRequestResponse extends Response {
  public BadRequestResponse(Request request, Resource resource, MimeTypes mimeTypes){
    super(request, resource, mimeTypes);
    this.setCode(500);
    this.setReasonPhrase("Bad Request");
  }

  @Override
  void send( OutputStream out ) {

  }
}
