import java.io.*;
import java.util.*;

public class Request {

  private String verb;
  private String uri;
  private String httpVersion;
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
      put( "TRACE", true );
      put( "CONNECT", true );
      put( "PATCH", true );
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

  public Request( InputStream client ) {
    this.headers = new HashMap<>();
    this.inputStream = client;
    this.inputStreamReader = new BufferedReader( new InputStreamReader( this.inputStream ) );
    this.parseRequest();
  }

  private void parseRequest( ) {
    try {
      this.parseFirstLine();
      this.parseHeaderSection();

      if ( this.headers.containsKey( "Content-Length" ) &&
          ( this.verb.equals( "PUT" ) || this.verb.equals( "POST" ) ) ) {
        this.parseBodySection();
      }

    } catch ( IOException e ) {
      e.printStackTrace();
    }
  }

  private void parseFirstLine( ) throws IOException {
    String[] lineSplit = this.inputStreamReader.readLine().split( " " );

    if ( isFirstLineOfRequest( lineSplit ) ) {
      this.setFirstLineOfRequest( lineSplit[0], lineSplit[1], lineSplit[2] );
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
    this.uri = uri;
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
    System.out.println( "header: " + currentHeader[0] + "   " + currentHeader[1] );
  }

  private void parseBodySection( ) {
    int contentLength = Integer.parseInt( this.headers.get( "Content-Length" ) );
    this.body = new byte[contentLength];

    try {
      this.inputStream.read( this.body );
    } catch ( IOException e ) {
      e.printStackTrace();
    }
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

}
