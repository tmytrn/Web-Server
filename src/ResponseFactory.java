import java.io.File;

public class ResponseFactory {
  Request request;
  Resource resource;
  MimeTypes mimeTypes;
  Htaccess htAccess;

  public ResponseFactory( Request request, Resource resource, MimeTypes mimeTypes ) {
    this.request = request;
    this.resource = resource;
    this.mimeTypes = mimeTypes;
  }

  public Response getResponse( Request request, Resource resource ) {
    //    if(this.resource.isProtected()){
//      this.htAccess = new Htaccess( resource.getHtAccessLocation() );
//
//      System.out.println( this.request.lookup( "Authorization" ) );
//      if(this.request.lookup( "Authorization" ) == null){
//        return new UnauthorizedResponse( request,resource );
//      }
//    }
    String verb = request.getVerb();
    if ( !new File( resource.getAbsolutePath() ).exists()  && !verb.equals( "PUT" )) {
      System.out.println( resource.getAbsolutePath() + "        path doesn't exist" );
      return new NotFoundResponse( request, resource, this.mimeTypes );
    } else if ( resource.isScript() ) {
      return new ScriptResponse( request, resource, this.mimeTypes );
    }
    

    switch ( verb ) {
      case "GET":
        Response getResponse = new GetResponse( request, resource, this.mimeTypes );
        return getResponse;
      case "HEAD":
        return new HeadResponse( request, resource, this.mimeTypes );
      case "POST":
        return new PostResponse( request, resource, this.mimeTypes );
      case "PUT":
        return new PutResponse( request, resource, this.mimeTypes );
      case "DELETE":
        return new DeleteResponse( request, resource, this.mimeTypes );
    }

    return null;
  }

}