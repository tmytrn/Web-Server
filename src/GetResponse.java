public class GetResponse extends Response {

  public GetResponse( Request request, Resource resource, MimeTypes mimeTypes){
  super(request, resource, mimeTypes);
  this.setCode(200);
  this.setReasonPhrase("OK");
  }



}
