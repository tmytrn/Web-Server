import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.*;
import java.util.*;
import java.util.Locale;
import java.util.TimeZone;

public class GetResponse extends Response {

  public GetResponse( Request request, Resource resource, MimeTypes mimeTypes ) {
    super( request, resource, mimeTypes );
    this.setCode( 200 );
    this.setReasonPhrase( "OK" );

    if(this.getRequest().getHeaders().containsKey( "If-Modified-Since" )){

      //header date
      Date headerModifiedDate;
      Date currentModifiedDate;

      System.out.println( "HELLO THERE" + this.getRequest().getHeaders().get( "If-Modified-Since" ) );
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "EEE, dd MMM yyyy HH:mm:ss " );
      try {
        headerModifiedDate = simpleDateFormat.parse( this.getRequest().getHeaders().get( "If-Modified-Since" ) );
        currentModifiedDate = simpleDateFormat.parse(  this.getLastModifiedDate( this.getResource().getFile() ) );
        if(!currentModifiedDate.after( headerModifiedDate )){
          this.setCode( 304 );
          this.setReasonPhrase( "Not Modified" );
        }
      } catch ( ParseException e ) {
        e.printStackTrace();
      }


    }
  }

  private String getLastModifiedDate(File file){
    SimpleDateFormat fileDateFormat = new SimpleDateFormat( "EEE, dd MMM yyyy HH:mm:ss " );
    return fileDateFormat.format( file.lastModified() ) + "GMT";
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
