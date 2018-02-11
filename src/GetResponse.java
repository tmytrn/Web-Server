public class GetResponse extends Response {

  public GetResponse( Request request, Resource resource){
  super(request, resource);
  this.code = 200;
  this.reasonPhrase = "OK";
  this.resource = resource;

  }



}
