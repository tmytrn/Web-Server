package http.response;

import java.io.OutputStream;

public class BadRequestResponse extends Response {
  public BadRequestResponse( ) {
    super();
    this.setCode( 400 );
    this.setReasonPhrase( "Bad http.request.Request" );
  }

  public void send( OutputStream out ) {
    String response = this.createDefaultHeaders();

    try {
      out.write( response.getBytes() );
      out.flush();
      out.close();
    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }

  public String createDefaultHeaders( ) {
    StringBuilder headers = new StringBuilder();

    headers.append( this.getCode() ).
        append( " " ).
        append( this.getReasonPhrase() ).
        append( "\n" ).
        append( "Server" ).
        append( ": " ).
        append( this.getResponseHeaders().get( "Server" ) ).
        append( "\n" ).
        append( "Date" ).
        append( ": " ).
        append( this.getResponseHeaders().get( "Date" ) ).
        append( "\r\n" );

    return headers.toString();

  }

}
