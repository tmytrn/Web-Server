package http.response;

import http.request.Request;
import http.resource.Resource;

import java.io.OutputStream;

public class NotFoundResponse extends Response {

  public NotFoundResponse( Request request, Resource resource ) {

    super( request, resource );
    this.setCode( 404 );
    this.setReasonPhrase( "Not Found" );

  }

  @Override
  public void send( OutputStream out ) {

    String response = this.createHeaders();

    try {
      out.write( response.getBytes() );
      out.flush();
      out.close();
    } catch ( Exception e ) {
      e.printStackTrace();
    }

  }

}
