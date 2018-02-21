package http.response;

import configurationreader.Htaccess;
import configurationreader.MimeTypes;
import http.request.Request;
import http.resource.Resource;

import java.io.File;

public class ResponseFactory {

  private Request request;
  private Resource resource;
  private MimeTypes mimeTypes;
  private Htaccess htAccess;

  public ResponseFactory( ) {
  }

  public Response getResponse( Request request, Resource resource, MimeTypes mimeTypes ) {

    this.request = request;
    this.resource = resource;
    this.mimeTypes = mimeTypes;

    Response protectedResponse = this.checkIfResourceIsProtected();
    Response scriptResponse = this.checkIfScriptResponse();

    if ( protectedResponse != null ) {
      return protectedResponse;
    } else if ( scriptResponse != null ) {
      return scriptResponse;
    }

    return this.performVerbResponse();

  }

  private Response checkIfResourceIsProtected( ) {

    if ( this.resource.isProtected() ) {
      this.htAccess = new Htaccess( this.resource.getHtAccessLocation() );

      if ( this.request.lookup( "Authorization" ) == null ) {
        return new UnauthorizedResponse( this.request, this.resource );
      }

      String authorizationInformation = this.request.lookup( "Authorization" );
      String[] credentials = authorizationInformation.split( " " );

      if ( !this.htAccess.isAuthorized( credentials[1] ) ) {
        return new ForbiddenResponse( this.request, this.resource );
      }
    }

    return null;

  }

  private Response checkIfScriptResponse( ) {

    if ( !new File( resource.getAbsolutePath() ).exists() &&
        !this.request.getVerb().equals( "PUT" ) ) {
      return new NotFoundResponse( request, resource );
    } else if ( resource.isScript() ) {
      return new ScriptResponse( request, resource );
    }

    return null;

  }

  private Response performVerbResponse( ) {

    switch ( this.request.getVerb() ) {
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
      default:
        return new ServerErrorResponse();
    }

  }

  public Response getServerErrorResponse( ) {

    return new ServerErrorResponse();

  }

  public Response getBadRequestResponse( ) {

    return new BadRequestResponse();

  }

}