import java.io.OutputStream;

public class PostResponse extends Response {

  public PostResponse(Request request, Resource resource, MimeTypes mimeTypes){
    super(request, resource, mimeTypes);
    this.setCode(200);
    this.setReasonPhrase("OK" );
  }

  void send( OutputStream out ) {

  }


}
