package logger;

import http.request.Request;
import http.response.Response;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {

  private File logFile;
  private FileWriter fileWriter;
  private BufferedWriter bufferedWriter;

  public Logger( String fileName ) {

    this.logFile = new File( fileName );

    if ( !this.logFile.exists() ) {
      this.logFile.getParentFile().mkdir();

      try {
        this.logFile.createNewFile();
      } catch ( Exception e ) {
        e.printStackTrace();
      }

    }

  }

  public void write( Request request, Response response, String IPAddress ) {

    try {
      this.fileWriter = new FileWriter( this.logFile, true );
      this.bufferedWriter = new BufferedWriter( this.fileWriter );
      String message = getLogMessage( request, response, IPAddress );
      this.bufferedWriter.append( message );
      this.bufferedWriter.close();
    } catch ( Exception e ) {
      e.printStackTrace();
    }

  }

  private String getLogMessage( Request request, Response response, String IPAddress ) {

    StringBuilder messageBuilder = new StringBuilder();

    messageBuilder.append( IPAddress ).
        append( " - " ).
        append( getUser( request ) ).
        append( " [" ).
        append( formatDate( response.getCalendar() ) ).
        append( "] " ).
        append( "\"" ).
        append( request.getFirstLineofRequest() ).
        append( "\" " ).
        append( response.getCode() ).
        append( " " ).
        append( getContentLength( response ) ).
        append( "\n" );

    return messageBuilder.toString();

  }

  private String formatDate( Calendar calendar ) {

    SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MMM/yyyy:HH:mm:ss Z" );

    return dateFormat.format( calendar.getTime() );

  }

  private String getUser( Request request ) {

    if ( request.lookup( "Authorization: " ) != null ) {
      return request.lookup( "Authorization: " );
    }

    return "-";

  }

  private String getContentLength( Response response ) {

    if ( response.getContentLength() > 0 ) {
      return Integer.toString( response.getContentLength() );
    }

    return "-";

  }

}
