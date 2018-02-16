
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.HashMap;

public class ScriptResponse extends Response {
  private byte[] output;
  private OutputStream outputStream;
  private InputStream inputStream;
  private ProcessBuilder processBuilder;
  private Map<String, String> environmentMap;

  public ScriptResponse( Request request, Resource resource, MimeTypes mimeTypes ) {
    super( request, resource, mimeTypes );
    processBuilder = new ProcessBuilder( resource.getAbsolutePath() );
    environmentMap = processBuilder.environment();
    setEnvironmentVariables( request, environmentMap );
    executeScript( request, processBuilder );
  }

  @Override
  void send( OutputStream out ) {

  }

  private void executeScript( Request request, ProcessBuilder processBuilder ) {
    try {
      Process process = processBuilder.start();
      outputStream = process.getOutputStream();
      outputStream.write( request.getBody() );
      process.waitFor();
      inputStream = process.getInputStream();
      output = new byte[inputStream.available()];
      inputStream.read(output);
    } catch ( Exception e ) {
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
      environmentMap.put( "HTTP_", request.lookup( ( String ) header ) );
    }

  }

}
