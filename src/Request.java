import java.io.*;
import java.util.*;

public class Request {

  private String verb;
  private String uri;
  private String httpVersion;
  private String queryString;
  private HashMap<String, String> headers;
  private byte[] body;
  private InputStream inputStream;
  private BufferedReader inputStreamReader;

  private static final HashMap<String, Boolean> VALID_HTTP_VERBS = new HashMap<String, Boolean>() {
    {
      put( "GET", true );
      put( "HEAD", true );
      put( "POST", true );
      put( "PUT", true );
      put( "DELETE", true );
    }
  };

  private static final HashMap<String, Boolean> VALID_HTTP_VERSIONS = new HashMap<String, Boolean>() {
    {
      put( "HTTP/1.0", true );
      put( "HTTP/1.1", true );
    }
  };

  public Request( String test ) {
    this.headers = new HashMap<>();

    StringTokenizer tokenizer = new StringTokenizer( test, "\n" );
    String currentLine;
    String[] currentLineSplit;

    while ( tokenizer.hasMoreTokens() ) {
      currentLine = tokenizer.nextToken();
      currentLineSplit = currentLine.split( " " );

      if ( isFirstLineOfRequest( currentLineSplit ) ) {
        this.setFirstLineOfRequest( currentLineSplit[0], currentLineSplit[1], currentLineSplit[2] );
      } else if ( currentLine.contains( ": " ) ) {
        this.parseHeader( currentLine );
      }

    }
  }

  public Request( InputStream client ) throws BadRequest {
    this.headers = new HashMap<>();
    this.inputStream = client;
    this.inputStreamReader = new BufferedReader( new InputStreamReader( this.inputStream ) );
    try{
      this.parseRequest();
    } catch ( Exception e ){
      e.printStackTrace();
      throw new BadRequest( "Bad Request" );
    }
  }

  private void parseRequest( ) throws BadRequest {
    try {
      this.parseFirstLine();
      this.parseHeaderSection();

      if ( this.headers.containsKey( "Content-Length" ) &&
          ( this.verb.equals( "PUT" ) || this.verb.equals( "POST" ) ) ) {
        this.parseBodySection();
      }

    } catch ( Exception e ) {
      e.printStackTrace();
      throw new BadRequest( "Bad Request" );
    }
  }


  private void parseFirstLine( ) throws IOException, BadRequest {
    String[] lineSplit = this.inputStreamReader.readLine().split( " " );

    if ( isFirstLineOfRequest( lineSplit ) ) {

      this.setFirstLineOfRequest( lineSplit[0], lineSplit[1], lineSplit[2] );
    }else{
      throw new BadRequest( "Bad Request" );
    }
  }

  private boolean isValidHTTPVerb( String verbToCheck ) {
    return VALID_HTTP_VERBS.containsKey( verbToCheck );
  }

  private boolean isValidHTTPVersion( String httpVersion ) {
    return VALID_HTTP_VERSIONS.containsKey( httpVersion );
  }

  private boolean isFirstLineOfRequest( String[] firstLineOfRequest ) {
    return firstLineOfRequest.length == 3 &&
        isValidHTTPVerb( firstLineOfRequest[0] ) &&
        isValidHTTPVersion( firstLineOfRequest[2] );
  }

  private void setFirstLineOfRequest( String verb, String uri, String httpVersion ) {
    this.verb = verb;
    if(hasQueryString( uri )){
      parseQueryString( uri );
    }else {
      this.uri = uri;
    }
    this.httpVersion = httpVersion;
    System.out.println( "verb is: " + this.verb );
    System.out.println( "uri is: " + this.uri );
    System.out.println( "httpVersion is: " + this.httpVersion );
  }

  private void parseHeaderSection( ) throws IOException {
    String lineRead;
    while ( this.inputStreamReader.ready() ) {
      lineRead = this.inputStreamReader.readLine();
      if ( lineRead.contains( ": " ) ) {
        this.parseHeader( lineRead );
      } else if ( lineRead.isEmpty() || lineRead.equals( "\r\n" ) ) {
        break;
      }
    }
  }

  private void parseHeader( String header ) {
    String[] currentHeader = header.split( ": " );

    this.headers.put( currentHeader[0], currentHeader[1] );
    System.out.println( "header: " + currentHeader[0] + " :  " + currentHeader[1] );
  }

  private void parseBodySection( ) {
    int contentLength = Integer.parseInt( this.headers.get( "Content-Length" ) );
    this.body = new byte[contentLength];

    try {

      for(int bodyIndex = 0; bodyIndex < contentLength; bodyIndex++){
        this.body[bodyIndex] = (byte) this.inputStreamReader.read();

      }
      System.out.println(new String(this.body));

    } catch ( IOException e ) {
      e.printStackTrace();
    }
  }
  private boolean hasQueryString(String uri){
    String [] uriDissect = uri.split( "\\?" );
    return (uriDissect.length > 1);
  }
  private void parseQueryString(String uri){
    String [] query = uri.split( "\\?" );
    this.queryString = query[1];
    this.uri = query[0];
  }

  public String getQueryString(){
    return this.queryString;
  }
  public HashMap<String, String> getHeaders(){
    return this.headers;
  }
  public String getUri( ) {
    return this.uri;
  }

  public byte[] getBody( ) {
    return this.body;
  }

  public String getVerb( ) {
    return this.verb;
  }

  public String getHttpVersion( ) {
    return this.httpVersion;
  }

  public String lookup( String header ) {
    return this.headers.get( header );
  }

  public HashMap<String, String> getHeaders() {
    return headers;
  }

}
