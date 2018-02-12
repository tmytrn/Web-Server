public class ResponseFactory {
  Request request;
  Resource resource ;
  public ResponseFactory(Request request, Resource resource){
    this.request = request;
    this.resource = resource;
  }
  public Response getResponse( Request request, Resource resource, MimeTypes mimeTypes){
  String verb = request.getVerb();
  switch(verb){
    case "GET":
      return new GetResponse(request, resource, mimeTypes);
    case "HEAD":
      return new HeadResponse(request,  resource, mimeTypes );
    case "POST":
      return new PostResponse( request, resource );
    case "PUT":
      return new PutResponse( request, resource );
    case "DELETE":
      return new DeleteResponse(request, resource);
  }
    return null;
  }

}