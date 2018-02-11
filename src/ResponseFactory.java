import Responses.Response;

public class ResponseFactory {

  public Response getResponse( Request request, Resource resource){
  String verb = request.getVerb();
  String httpVersion = request.getHttpVersion();

  if(resource.isProtected()){


  }

  switch(verb){
    case "GET":
      return new GetResponse(request, resource);
    case "HEAD":
      return new HeadResponse(request,  resource );
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