package configurationreader;

import java.util.*;

public class MimeTypes extends ConfigurationReader {

  private HashMap<String, String> types;

  public MimeTypes( String fileName ) {

    super( fileName );
    this.types = new HashMap<>();

  }

  public HashMap<String, String> getTypes( ) {

    return this.types;

  }

  public boolean startsWithAHashSymbol( String stringToBeChecked ) {

    return stringToBeChecked.startsWith( "#" );

  }

  public boolean isEmptyString( String stringToBeChecked ) {

    return stringToBeChecked.equals( "" );

  }

  public void skipUselessLines( ) {

    while ( startsWithAHashSymbol( this.getCurrentLine() ) || isEmptyString( this.getCurrentLine() ) ) {
      this.setNextLine();
    }

  }

  public void tokenizeCurrentLine( ) {

    StringTokenizer tokens = new StringTokenizer( this.getCurrentLine() );
    String valueToken;
    String keyToken;

    if ( tokens.countTokens() > 1 ) {
      valueToken = tokens.nextToken();

      while ( tokens.hasMoreTokens() ) {
        keyToken = tokens.nextToken();
        this.types.put( keyToken, valueToken );
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

  public String lookup( String extension ) {

    return this.types.get( extension );

  }

}