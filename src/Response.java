import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class Response {
  public int code;
  public String reasonPhrase;
  public Resource resource;
  public Request request;
  private MimeTypes mimeTypes;
  private LinkedHashMap<String, String> responseHeaders;

  public Response(Request request, Resource resource){
    this.request = request;
    this.resource = resource;
    responseHeaders = new LinkedHashMap<>();
    responseHeaders.put( "Date", getDate() );
    responseHeaders.put( "Server", "web-server-lookin-like-a-snack" );
    if ( resource != null ) {
      buildResourceHeaders();
    }
  }
  public Response( Request request, Resource resource , MimeTypes mimeTypes) {
    this.request = request;
    this.resource = resource;
    this.mimeTypes = mimeTypes;
    responseHeaders = new LinkedHashMap<>();
    responseHeaders.put( "Date", getDate() );
    responseHeaders.put( "Server", "web-server-lookin-like-a-snack" );
    if ( resource != null ) {
      buildResourceHeaders();
    }
  }

  public void send( OutputStream out ) {
    String response = this.createHeaders();
    try {
      String res = new String(response.getBytes());
      System.out.println(res);
      out.write( response.getBytes() );
      out.flush();
      sendResource( out );
      out.flush();
      out.close();
    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }
  private void sendResource( OutputStream out ) { // made into buffered output stream
    if ( resource != null ) {
      File file = resource.getFile();
      byte[] fileBytes = new byte[( int ) file.length()];
      try {
        FileInputStream fileToArray = new FileInputStream( file );
        fileToArray.read( fileBytes );
        out.write( fileBytes );
      } catch ( Exception e ) {

      }
    }
  }

  public String createHeaders( ) {
    StringBuilder headers = new StringBuilder();
    headers.append( topLine() );
    for ( String key : this.responseHeaders.keySet() ) {
//      headers.append( key + ": " + this.responseHeaders.get( key ) + "\r\n" );
      headers.append( key ).append( ": " ).append( this.responseHeaders.get( key ) ).append( "\n" );
    }
    headers.append( "\r\n" );
    return headers.toString();
  }

  public String topLine( ) {
    return request.getHttpVersion() + " " + this.code + " " + this.reasonPhrase + "\r\n";
  }

  public String getDate( ) {
    ZonedDateTime httpDate = ZonedDateTime.now( ZoneOffset.UTC );
    return httpDate.format( DateTimeFormatter.ofPattern( "EEE, dd MMM yyyy hh:mm:ss " ) ) + "GMT";
  }

  private void buildResourceHeaders( ) {
    File content = resource.getFile();
    SimpleDateFormat fileDateFormat = new SimpleDateFormat( "EEE, dd MMM yyyy hh:mm:ss " );
    responseHeaders.put( "Last Modified", fileDateFormat.format( content.lastModified() ) + "GMT" );
    responseHeaders.put( "Content-Length", String.valueOf( content.length() ) );
    responseHeaders.put( "Content-Type", getMimeType( content ) + "; charset=utf-8" ); //include charset
    System.out.println( this.responseHeaders.get( "Content-Length" ) );
  }
  private String getMimeType(File file){
    String fileName = file.getName();
    String[] type = fileName.split("\\.");
    //System.out.println(mimeTypes.lookup(type[1] ));
    return mimeTypes.lookup( type[1]);
  }

  public String getLogDate( ) {
    SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MMM/yyyy hh:mm:ss Z" );
    String date = dateFormat.format( new Date() );
    return date;
  }
}
