import java.util.*;
import java.util.stream.*;

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

  public Request( Stream client ) {


  }

  public void parse( ) {

  }

  public boolean isValidHTTPVerb( String verbToCheck ) {
    return VALIDHTTPVERBS.containsKey( verbToCheck );
  }

  public boolean isValidHTTPVersion( String httpVersion ) {
    return VALIDHTTPVERSIONS.containsKey( httpVersion );
  }

  public boolean isFirstLineOfRequest( String[] firstLineOfRequest ) {
    return firstLineOfRequest.length == 3 &&
        isValidHTTPVerb( firstLineOfRequest[0] ) &&
        isValidHTTPVersion( firstLineOfRequest[2] );
  }

  public void setFirstLineOfRequest( String verb, String uri, String httpVersion ) {
    this.verb = verb;
    this.uri = uri;
    this.httpVersion = httpVersion;
    System.out.println( "verb is: " + this.verb );
    System.out.println( "uri is: " + this.uri );
    System.out.println( "httpVersion is: " + this.httpVersion );
  }

  public void parseHeader( String header ) {
    String[] currentHeader = header.split( ": " );
    this.headers.put( currentHeader[0], currentHeader[1] );
    System.out.println( "header: " + currentHeader[0] + "   " + currentHeader[1] );
  }

//  public String getUri(){
//
//  }

//  public byte[] getBody{
//
//  }

//  public String getVerb(){
//
//  }

//  public String getHttpVersion(){
//
//  }

//  public String lookup(String header){
//
//  }

}
