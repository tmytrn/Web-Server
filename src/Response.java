import java.io.*;
import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public abstract class Response {
  private int code;
  private String reasonPhrase;
  private Resource resource;
  private Request request;
  private HashMap<String, String> responseHeaders;

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
    ZonedDateTime httpDate = ZonedDateTime.now( ZoneOffset.UTC );
    return httpDate.format( DateTimeFormatter.ofPattern( "EEE, dd MMM yyyy hh:mm:ss " ) ) + "GMT";
  }

  abstract void send( OutputStream out );

  public String createHeaders( ) {
    StringBuilder headers = new StringBuilder();
    headers.append( firstHeadersLine() );
    for ( String key : this.responseHeaders.keySet() ) {
      headers.append( key ).append( ": " ).append( this.responseHeaders.get( key ) ).append( "\n" );
    }
    headers.append( "\r\n" );
    return headers.toString();
  }

  public String firstHeadersLine( ) {
    return this.request.getHttpVersion() + " " + this.code + " " + this.reasonPhrase + "\r\n";
  }

//  public String getLogDate( ) {
//    SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MMM/yyyy hh:mm:ss Z" );
//    String date = dateFormat.format( new Date() );
//    return date;
//  }

  public int getCode(){
    return this.code;
  }

  public String getReasonPhrase(){
    return this.reasonPhrase;
  }

  public Resource getResource(){
    return this.resource;
  }

  public Request getRequest(){
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

}
