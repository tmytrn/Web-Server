

public class PostResponse extends Response {
  public PostResponse(Request request, Resource resource){
    super(request, resource);
    this.code = 200;
    this.reasonPhrase = "OK";
    this.resource = resource;

  }

}
