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
    if ( this.resource.isProtected() ) {
      this.htAccess = new Htaccess( resource.getHtAccessLocation() );

      System.out.println( this.request.lookup( "Authorization" ) );
      if ( this.request.lookup( "Authorization" ) == null ) {
        return new UnauthorizedResponse( request, resource );
      }
      String authInfo = this.request.lookup( "Authorization" );
      String[] credentials = authInfo.split( " " );
      if ( !this.htAccess.isAuthorized( credentials[1] ) ) {
        return new ForbiddenResponse( request, resource );
      }
    }

    String verb = request.getVerb();
    if ( !new File( resource.getAbsolutePath() ).exists() && !verb.equals( "PUT" ) ) {
      System.out.println( resource.getAbsolutePath() + "        path doesn't exist" );
      return new NotFoundResponse( request, resource );
    } else if ( resource.isScript() ) {
      return new ScriptResponse( request, resource );
    }


    switch ( verb ) {
      case "GET":
        return new GetResponse( request, resource, this.mimeTypes );
      case "HEAD":
        return new HeadResponse( request, resource, this.mimeTypes );
      case "POST":
        return new PostResponse( request, resource );
      case "PUT":
        return new PutResponse( request, resource );
      case "DELETE":
        return new DeleteResponse( request, resource );
      default:
        return new ServerErrorResponse( request, resource );
    }
  }

  public Response getServerErrorResponse( ) {
    return new ServerErrorResponse( this.request, this.resource );
  }

}