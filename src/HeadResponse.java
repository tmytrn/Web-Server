

public class HeadResponse extends Response {
  public HeadResponse(Request request, Resource resource){
    super(request, resource);
    this.code = 200;
    this.reasonPhrase = "OK";
    this.resource = resource;

  }
}
