import sun.java2d.pipe.SpanShapeRenderer;

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
  private MimeTypes mimeTypes;
  private HashMap<String, String> responseHeaders;

  public Response( Request request, Resource resource, MimeTypes mimeTypes ) {
    this.request = request;
    this.resource = resource;
    this.mimeTypes = mimeTypes;
    this.responseHeaders = new HashMap();
    this.putDefaultHeaders();
  }

  public void putDefaultHeaders( ) {
    this.responseHeaders.put( "Date", getDate() );
    this.responseHeaders.put( "Server", "web-server-lookin-like-a-snack" );
  }

  private void putResourceHeaders( ) {
    File content = this.getResource().getFile();
    SimpleDateFormat fileDateFormat = new SimpleDateFormat( "EEE, dd MMM yyyy HH:mm:ss z" );
    this.getResponseHeaders().put( "Last-Modified", fileDateFormat.format( content.lastModified() ) );
    this.getResponseHeaders().put( "Content-Length", String.valueOf( content.length() ) );
    this.getResponseHeaders().put( "Content-Type", getMimeType( content ) + "; charset=utf-8" );
  }

  private String getMimeType( File file ) {
    String fileName = file.getName();
    String[] type = fileName.split( "\\." );

    if( mimeTypes.lookup( type[type.length - 1] ) != null) {
      return mimeTypes.lookup( type[type.length - 1] );
    }

    return "text/text";
  }

  public String getDate( ) {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat( "EEE, dd MMM yyyy HH:mm:ss z" );
    return dateFormat.format( calendar.getTime());
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
    return this.request.getHttpVersion() + " " + this.code + " " + this.reasonPhrase + "\n";
  }

//  public String getLogDate( ) {
//    SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MMM/yyyy hh:mm:ss Z" );
//    String date = dateFormat.format( new Date() );
//    return date;
//  }

  public int getCode(){
    return this.code;
  }

  private boolean sendingFile() {
    String verb = request.getVerb();
    return verb.equals( "GET" );
  }

  public String getReasonPhrase(){
    return this.reasonPhrase;
  }

  public Resource getResource(){
    return this.resource;
  }

//  private String getMimeType(File file){
//      String fileName = file.getName();
//      String[] type = fileName.split( "\\." );
//      return mimeTypes.lookup( type[1] );
//    }

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
