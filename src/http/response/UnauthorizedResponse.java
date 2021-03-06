package http.response;

import http.request.Request;
import http.resource.Resource;

import java.io.OutputStream;

public class UnauthorizedResponse extends Response {

  public UnauthorizedResponse( Request request, Resource resource ) {

    super( request, resource );
    this.setCode( 401 );
    this.setReasonPhrase( "Unauthorized" );

  }

  public void send( OutputStream out ) {

    String response = this.firstHeadersLine() + getWWWResponseHeader() + "\r\n";
    try {
      out.write( response.getBytes() );
      out.flush();
      out.close();
    } catch ( Exception e ) {
      e.printStackTrace();
    }

  }

  private String getWWWResponseHeader( ) {

    return "WWW-Authenticate: Basic";

  }


}

