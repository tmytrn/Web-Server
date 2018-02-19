
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.HashMap;

public class ScriptResponse extends Response {

  private byte[] output;
  private byte[] errorOutput;
  private OutputStream outputStream;
  private InputStream inputStream;
  private InputStream errorStream;
  private ProcessBuilder processBuilder;
  private Map<String, String> environmentMap;

  public ScriptResponse( Request request, Resource resource ) {

    super( request, resource );
    this.processBuilder = new ProcessBuilder( resource.getAbsolutePath() );
    this.environmentMap = this.processBuilder.environment();
    setEnvironmentVariables( request, this.environmentMap );
    executeScript( request, this.processBuilder );

  }

  public void send( OutputStream out ) {

    String stringResponse = buildStatusHeader();
    byte[] byteResponse = stringResponse.getBytes();

    try {
      out.write( byteResponse );
      out.flush();
      sendResource( out );
      out.flush();
      out.close();
    } catch ( Exception e ) {
      e.printStackTrace();
    }

  }

  private String buildStatusHeader( ) {

    if ( this.errorOutput != null && this.output.length > 0 ) {
      this.setCode( 200 );
      this.setReasonPhrase( "OK" );
    } else {
      this.setCode( 500 );
      this.setReasonPhrase( "Internal Server Error" );
    }

    StringBuilder header = new StringBuilder( firstHeadersLine() );

    return header.toString();

  }

  private void sendResource( OutputStream out ) {

    try {
      if ( this.output != null && this.output.length > 0 ) {
        out.write( this.output );
        out.flush();
      } else if ( this.errorOutput != null && this.output.length > 0 ) {
        out.write( this.errorOutput );
        out.flush();
      }

    } catch ( Exception e ) {
      e.printStackTrace();
      sendServerErrorResponse( out );
    }

  }

  private void executeScript( Request request, ProcessBuilder processBuilder ) {

    try {
      Process process = processBuilder.start();
      this.outputStream = process.getOutputStream();

      if ( request.getBody() != null ) {
        this.outputStream.write( request.getBody() );
      }

      process.waitFor();
      processOutput( process );
      processErrorOutput( process );

    } catch ( Exception e ) {
      e.printStackTrace();
      this.sendServerErrorResponse( this.outputStream );
    }
  }

  private void processOutput( Process process ) {

    try {
      this.inputStream = process.getInputStream();
      this.output = new byte[this.inputStream.available()];
      this.inputStream.read( this.output );
    } catch ( Exception e ) {
      e.printStackTrace();
      this.sendServerErrorResponse( this.outputStream );
    }

  }

  private void processErrorOutput( Process process ) {

    try {
      process.getErrorStream();
      this.errorStream = process.getInputStream();
      this.errorOutput = new byte[this.errorStream.available()];
      this.errorStream.read( this.errorOutput );
    } catch ( Exception e ) {
      e.printStackTrace();
      this.sendServerErrorResponse( this.outputStream );
    }

  }

  private void setEnvironmentVariables( Request request, Map<String, String> environmentMap ) {

    environmentMap.put( "SERVER_PROTOCOL", request.getHttpVersion() );

    if ( request.getQueryString() != null && request.getQueryString().length() > 0 ) {
      environmentMap.put( "QUERY_STRING", request.getQueryString() );
    }

    HashMap headers = request.getHeaders();

    for ( Object header : headers.keySet() ) {
      environmentMap.put( "HTTP_" + header, request.lookup( ( String ) header ) );
    }

  }

}
