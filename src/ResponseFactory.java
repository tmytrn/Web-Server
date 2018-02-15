import java.io.File;

public class ResponseFactory {
  Request request;
  Resource resource;
  MimeTypes mimeTypes;

  public ResponseFactory( Request request, Resource resource, MimeTypes mimeTypes) {
    this.request = request;
    this.resource = resource;
    this.mimeTypes = mimeTypes;
  }

  public Response getResponse( Request request, Resource resource) {
    //check authorization
    if ( !new File( resource.getAbsolutePath() ).exists() ) {
      return new NotFoundResponse( request, resource );
    }
    else if(resource.isScript()){

    }
    String verb = request.getVerb();
    switch ( verb ) {
      case "GET":
        return new GetResponse( request, resource, mimeTypes );
      case "HEAD":
        return new HeadResponse( request, resource, mimeTypes );
      case "POST":
        return new PostResponse( request, resource );
      case "PUT":
        return new PutResponse( request, resource );
      case "DELETE":
        return new DeleteResponse( request, resource );
    }
    return null;
  }

}