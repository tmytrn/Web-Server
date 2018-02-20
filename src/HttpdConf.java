import java.util.*;

public class HttpdConf extends ConfigurationReader {

  private HashMap<String, String> configuration;
  private HashMap<String, String> scriptAliasMap;
  private HashMap<String, String> aliasMap;

  public HttpdConf( String fileName ) {

    super( fileName );
    this.configuration = new HashMap<>();
    this.scriptAliasMap = new HashMap<>();
    this.aliasMap = new HashMap<>();

  }

  public void skipUselessLines( ) {

    while ( isEmptyString( this.getCurrentLine() ) || startsWithAHashSymbol( this.getCurrentLine() ) ) {
      this.setNextLine();
    }

  }

  public boolean hasAlias( String key ) {

    return key.equals( "Alias" );

  }

  public boolean hasScriptAlias( String key ) {

    return key.equals( "ScriptAlias" );

  }

  public boolean startsWithAHashSymbol( String stringToBeChecked ) {

    return stringToBeChecked.startsWith( "#" );

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

  public void addToAliasMap( String key, String value ) {

    this.aliasMap.put( key, value );

  }

  public void addToScriptAliasMap( String key, String value ) {

    this.scriptAliasMap.put( key, value );

  }

  public void tokenizeCurrentLine( ) {

    StringTokenizer tokens = new StringTokenizer( this.getCurrentLine() );
    String valueToken;
    String keyToken = tokens.nextToken();

    while ( tokens.hasMoreTokens() ) {
      if ( hasAlias( keyToken ) ) {
        addToAliasMap( tokens.nextToken(), removeQuotes( tokens.nextToken() ) );
      } else if ( hasScriptAlias( keyToken ) ) {
        addToScriptAliasMap( tokens.nextToken(), removeQuotes( tokens.nextToken() ) );
      } else {
        valueToken = tokens.nextToken();
        this.configuration.put( keyToken, removeQuotes( valueToken ) );
      }
    }

    this.setNextLine();

  }

  public void load( ) {

    while ( this.hasMoreLines() ) {
      this.skipUselessLines();
      this.tokenizeCurrentLine();
    }

  }

  public String lookupConfiguration( String string ) {

    return configuration.get( string );

  }

  public String lookupAlias( String alias ) {

    return this.aliasMap.get( alias );

  }

  public String lookupScript( String script ) {

    return this.scriptAliasMap.get( script );

  }

  public HashMap<String, String> getConfiguration( ) {

    return this.configuration;

  }

  public HashMap<String, String> getAliasMap( ) {

    return this.aliasMap;

  }

  public HashMap<String, String> getScriptAliasMap( ) {

    return this.scriptAliasMap;

  }

}