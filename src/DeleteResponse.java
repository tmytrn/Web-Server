import Responses.Response;

public class DeleteResponse extends Response {
  public DeleteResponse( Request request, Resource resource){
    super(request,  resource);
    this.code = 204;
    this.reasonPhrase = "No Content";
    this.resource = resource;

  }
}
