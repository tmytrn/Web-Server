import java.io.*;
import java.text.*;
import java.util.*;


public class GetResponse extends Response {

  public GetResponse( Request request, Resource resource, MimeTypes mimeTypes ) {
    super( request, resource, mimeTypes );
    this.setCode( 200 );
    this.setReasonPhrase( "OK" );
    this.putResourceHeaders();
    this.changeResponseIfResourcedCached();
  }

  private void changeResponseIfResourcedCached(){
    if(this.getRequest().getHeaders().containsKey( "If-Modified-Since" )){
      Date headerModifiedDate;
      Date currentModifiedDate = this.getResource().getLastModifiedDate();
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "EEE, dd MMM yyyy HH:mm:ss " );

      try {
        headerModifiedDate = simpleDateFormat.parse( this.getRequest().getHeaders().get( "If-Modified-Since" ) );
        if(!currentModifiedDate.after( headerModifiedDate )){
          this.setCode( 304 );
          this.setReasonPhrase( "Not Modified" );
        }
      } catch ( ParseException e ) {
        e.printStackTrace();
      }
    }
  }

  private void putResourceHeaders( ) {
    File content = this.getResource().getFile();
    setContentLength((int) content.length() );
    if(content != null) {
      this.getResponseHeaders().put( "Last-Modified", getLastModifiedDate( content ) );
      this.getResponseHeaders().put( "Content-Length", String.valueOf( content.length() ) );
      this.getResponseHeaders().put( "Content-Type", getMimeType( content ) + "; charset=utf-8" );
    }
  }

  private String getLastModifiedDate(File file){
    SimpleDateFormat fileDateFormat = new SimpleDateFormat( "EEE, dd MMM yyyy HH:mm:ss z" );
    return fileDateFormat.format( file.lastModified() );
  }

  private String getMimeType( File file ) {
    String fileName = file.getName();
    String[] type = fileName.split( "\\." );

    if( this.getMimeTypes().lookup( type[type.length - 1] ) != null) {
      return this.getMimeTypes().lookup( type[type.length - 1] );
    }

    return "text/text";
  }

  public void send( OutputStream out ) {
    String response = this.createHeaders();
    try {
      String res = new String( response.getBytes() );
      System.out.println( res );
      out.write( response.getBytes() );
      out.flush();
      if(this.getCode() == 200){
        sendResource( out );
        out.flush();
      }
      out.close();
    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }

  private void sendResource( OutputStream out ) { // made into buffered output stream
    File file = this.getResource().getFile();
    byte[] fileBytes = new byte[( int ) file.length()];
    try {
      FileInputStream fileToArray = new FileInputStream( file );
      fileToArray.read( fileBytes );
      out.write( fileBytes );
    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }

}
