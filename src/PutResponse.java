

public class PutResponse extends Response {
  public PutResponse(Request request, Resource resource){
    super(request, resource);
    this.code = 201;
    this.reasonPhrase = "Created";
    this.resource = resource;

  }
}
