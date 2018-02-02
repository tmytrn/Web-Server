import java.util.StringTokenizer;
import java.util.*;

public class HttpdConf extends ConfigurationReader{
  private HashMap<String, String> configuration;

  public HttpdConf(String fileName){
    super(fileName);
    configuration = new HashMap<>();
  }


  public void skipUselessLines( ) {
    while (isEmptyString( this.getCurrentLine() )) {
      this.setNextLine();
    }
  }
  public boolean hasAlias(String key){
    if(key.contains( "Alias" ))
      return true;
    else
      return false;
  }

  public boolean isEmptyString( String stringToBeChecked ) {
    return stringToBeChecked.equals( "" );
  }
  public String removeQuotes(String path){
    String returnString;
      if(path.startsWith( "\"" ))
      returnString = path.substring( 1, path.length()-1 );
      else
        returnString = path;
    return returnString;
  }


  public void tokenizeCurrentLine( ) {
    StringTokenizer tokens = new StringTokenizer( this.getCurrentLine() );
    String valueToken;
    String keyToken;

      keyToken = tokens.nextToken();
      if(hasAlias( keyToken )){
        keyToken = tokens.nextToken();
      }

      while ( tokens.hasMoreTokens() ) {
        valueToken = removeQuotes(tokens.nextToken());
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
  public String lookup( String string){
    return configuration.get( string );
  }

}