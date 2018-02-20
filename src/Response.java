import java.io.*;
import java.text.*;
import java.util.*;

public abstract class Response {

  private int code;
  private int contentLength;
  private String reasonPhrase;
  private Resource resource;
  private Request request;
  private HashMap<String, String> responseHeaders;
  private Calendar calendar;

  public Response( ) {

    this.responseHeaders = new HashMap();
    this.putDefaultHeaders();

  }

  public Response( Request request, Resource resource ) {

    this.request = request;
    this.resource = resource;
    this.responseHeaders = new HashMap();

    this.putDefaultHeaders();

  }

  public void putDefaultHeaders( ) {

    this.responseHeaders.put( "Date", getDate() );
    this.responseHeaders.put( "Server", "web-server-lookin-like-a-snack" );

  }

  public String getDate( ) {

    this.calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat( "EEE, dd MMM yyyy HH:mm:ss z" );

    return dateFormat.format( calendar.getTime() );

  }

  public abstract void send( OutputStream out );

  public String createHeaders( ) {

    StringBuilder headers = new StringBuilder();

    headers.append( this.firstHeadersLine() );
    for ( String key : this.responseHeaders.keySet() ) {
      headers.append( key ).
          append( ": " ).
          append( this.responseHeaders.get( key ) ).
          append( "\n" );
    }
    headers.append( "\r\n" );

    return headers.toString();

  }

  public String firstHeadersLine( ) {

    return this.request.getHttpVersion() + " " + this.code + " " + this.reasonPhrase + "\n";

  }

  public int getCode( ) {

    return this.code;

  }

  public String getReasonPhrase( ) {

    return this.reasonPhrase;

  }

  public Resource getResource( ) {

    return this.resource;

  }

  public Request getRequest( ) {

    return this.request;

  }

  public HashMap<String, String> getResponseHeaders( ) {

    return responseHeaders;

  }

  public byte[] getRequestBody( ) {

    return this.request.getBody();

  }

  public void setCode( int responseCode ) {

    this.code = responseCode;

  }

  public void setReasonPhrase( String reasonPhrase ) {

    this.reasonPhrase = reasonPhrase;

  }

  public int getContentLength( ) {

    return this.contentLength;

  }

  public Calendar getCalendar( ) {

    return this.calendar;

  }

  public void setContentLength( int length ) {

    this.contentLength = length;

  }

  public void sendServerErrorResponse( OutputStream outputStream ) {

    ResponseFactory responseFactory = new ResponseFactory();
    Response serverErrorResponse = responseFactory.getServerErrorResponse();
    serverErrorResponse.send( outputStream );

  }

}
