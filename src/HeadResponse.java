

public class HeadResponse extends Response {
  public HeadResponse(Request request, Resource resource, MimeTypes mimeTypes){
    super(request, resource, mimeTypes);
    this.code = 200;
    this.reasonPhrase = "OK";
    this.resource = resource;
  }
}
