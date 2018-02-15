
public class BadRequestResponse extends Response {
  public BadRequestResponse(Request request, Resource resource){
    super(request, resource);
    this.setCode(500);
    this.setReasonPhrase("Bad Request");
  }
}
