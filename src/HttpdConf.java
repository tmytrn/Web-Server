import java.util.*;

public class HttpdConf extends ConfigurationReader {
  private HashMap<String, String> configuration;

  public HttpdConf( String fileName ) {
    super( fileName );
    this.configuration = new HashMap<>();
  }


  public void skipUselessLines( ) {
    while ( isEmptyString( this.getCurrentLine() ) ) {
      this.setNextLine();
    }
  }

  public boolean hasAlias( String key ) {
    return key.contains( "Alias" );
  }

  public boolean isEmptyString( String stringToBeChecked ) {
    return stringToBeChecked.equals( "" );
  }

  public String removeQuotes( String path ) {
    if ( path.startsWith( "\"" ) ) {
      return path.substring( 1, path.length() - 1 );
    }

    return path;
  }

  public void tokenizeCurrentLine( ) {
    StringTokenizer tokens = new StringTokenizer( this.getCurrentLine() );
    String valueToken;
    String keyToken = tokens.nextToken();

    if ( hasAlias( keyToken ) ) {
      keyToken = tokens.nextToken();
    }

    while ( tokens.hasMoreTokens() ) {
      valueToken = removeQuotes( tokens.nextToken() );
      configuration.put( keyToken, valueToken );
    }

    this.setNextLine();
  }

  public void load( ) {
    while ( this.hasMoreLines() ) {
      this.skipUselessLines();
      this.tokenizeCurrentLine();
    }
  }

  public String lookup( String string ) {
    return configuration.get( string );
  }

}