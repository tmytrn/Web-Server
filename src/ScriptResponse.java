
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

  public ScriptResponse( Request request, Resource resource, MimeTypes mimeTypes ) {
    super( request, resource, mimeTypes );
    processBuilder = new ProcessBuilder( resource.getAbsolutePath() );
    environmentMap = processBuilder.environment();
    setEnvironmentVariables( request, environmentMap );
    executeScript( request, processBuilder );
  }

  void send( OutputStream out ) {
    String stringResponse = buildStatusHeader();
    byte[] byteResponse = stringResponse.getBytes();

    try {
      System.out.println( stringResponse );
      out.write( byteResponse );
      out.flush();
      sendResouce( out );
      out.flush();
      out.close();
    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }

  private String buildStatusHeader( ) {
    if ( errorOutput != null && output.length > 0 ) {
      this.setCode( 200 );
      this.setReasonPhrase( "OK" );
    } else {
      this.setCode( 500 );
      this.setReasonPhrase( "Internal Server Error" );
    }
    StringBuilder header = new StringBuilder( firstHeadersLine() );
    return header.toString();

  }

  private void sendResouce( OutputStream out ) {
    try {
      if ( output != null && output.length > 0 ) {
        out.write( output );
        out.flush();
      } else if ( errorOutput != null && output.length > 0 ) {
        out.write( errorOutput );
        out.flush();
      }
    } catch ( Exception e ) {
      e.printStackTrace();
      //500
    }

  }

  private void executeScript( Request request, ProcessBuilder processBuilder ) {
    try {
      Process process = processBuilder.start();
      outputStream = process.getOutputStream();
      if ( request.getBody() != null ) {
        outputStream.write( request.getBody() );
      }
      process.waitFor();
      processOutput( process );
      processErrorOutput( process );
    } catch ( Exception e ) {
      e.printStackTrace();
      //500
    }
  }

  private void processOutput( Process process ) {
    try {
      inputStream = process.getInputStream();
      output = new byte[inputStream.available()];
      inputStream.read( output );
    } catch ( Exception e ) {
      e.printStackTrace();
      //500
    }
  }

  private void processErrorOutput( Process process ) {
    try {
      process.getErrorStream();
      errorStream = process.getInputStream();
      errorOutput = new byte[errorStream.available()];
      errorStream.read( errorOutput );
    } catch ( Exception e ) {
      e.printStackTrace();
      //500
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
