

public class HeadResponse extends Response {
  public HeadResponse(Request request, Resource resource){
    super(request, resource);
    this.setCode(200);
    this.setReasonPhrase("OK");
  }
}
