import java.io.*;
import java.util.*;

public class Request {

  private String verb;
  private String uri;
  private String httpVersion;
  private HashMap<String, String> headers;
  private byte[] body;

  private static final HashMap<String, Boolean> VALIDHTTPVERBS = new HashMap<String, Boolean>() {
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

  private static final HashMap<String, Boolean> VALIDHTTPVERSIONS = new HashMap<String, Boolean>() {
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

//    BufferedReader bufferedReader = new BufferedReader( new InputStreamReader (client) );
//
//    String lineRead;
//    String[] lineReadSplit;
//
//    try {
//      lineRead = bufferedReader.readLine();
//
//      //reading first line
//      lineReadSplit = lineRead.split( " " );
//      if ( isFirstLineOfRequest( lineReadSplit ) ) {
//        this.setFirstLineOfRequest( lineReadSplit[0], lineReadSplit[1], lineReadSplit[2] );
//      }else{
//        //error
//      }
//
//      //parsing headers
//      while(!lineRead.equals( "" )){
//        lineRead = bufferedReader.readLine();
//        parseHeader( lineRead );
//      }
//
//    } catch ( IOException e ) {
//      e.printStackTrace();
//    }
   this.parseRequest( client );

  }

  private void parseRequest( InputStream requestInputStream ) {
    try {
      String lineRead = getNextLine( requestInputStream );

      this.parseFirstLine( lineRead );

      this.parseHeaderSection( requestInputStream );

      if ( this.headers.containsKey( "Content-Length" ) &&
          ( this.verb.equals( "PUT" ) || this.verb.equals( "POST" ) ) ) {
        this.parseBodySection( requestInputStream );
      }

    } catch ( IOException e ) {
      e.printStackTrace();
    }
  }

  private String getNextLine( InputStream inputStream ) throws IOException {
    StringBuilder inputStreamRead = new StringBuilder();
    char characterRead;

    while ( ( characterRead = ( char ) inputStream.read() ) > 0 ) {
      inputStreamRead.append( characterRead );

      if ( characterRead == '\r' ) {
        char newLineBreak = ( char ) inputStream.read();
        if ( newLineBreak == '\n' ) {
          inputStreamRead.deleteCharAt( inputStreamRead.lastIndexOf( "\r" ) );
          return inputStreamRead.toString();
        }
      }
    }

    return inputStreamRead.toString();
  }

  private void parseFirstLine( String firstLine ) {
    String[] lineSplit;
    lineSplit = firstLine.split( " " );

    if ( isFirstLineOfRequest( lineSplit ) ) {
      this.setFirstLineOfRequest( lineSplit[0], lineSplit[1], lineSplit[2] );
    }
  }

  private boolean isValidHTTPVerb( String verbToCheck ) {
    return VALIDHTTPVERBS.containsKey( verbToCheck );
  }

  private boolean isValidHTTPVersion( String httpVersion ) {
    return VALIDHTTPVERSIONS.containsKey( httpVersion );
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

  private void parseHeaderSection( InputStream inputStreamHeader ) throws IOException {
    String lineRead;

    while ( inputStreamHeader.available() > 0 ) {
      lineRead = getNextLine( inputStreamHeader );
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

  private void parseBodySection( InputStream inputStreamBody ) {
    int contentLength = Integer.parseInt( this.headers.get( "Content-Length" ) );
    this.body = new byte[contentLength];

    try {
      inputStreamBody.read( this.body );
      System.out.println(new String(this.body));
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
