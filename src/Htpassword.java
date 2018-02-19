import java.util.HashMap;
import java.util.Base64;
import java.nio.charset.Charset;
import java.security.MessageDigest;

import java.io.IOException;

public class Htpassword extends ConfigurationReader {
  private HashMap<String, String> passwords;

  public Htpassword( String filename ) throws IOException {
    super( filename );
    this.passwords = new HashMap<>();
    this.load();
  }

  public void load( ) {
    while ( this.hasMoreLines() ) {
      this.parseLine( this.getCurrentLine() );
      this.setNextLine();
    }
  }

  protected void parseLine( String line ) {
    String[] tokens = line.split( ":" );

    if ( tokens.length == 2 ) {
      passwords.put( tokens[0], tokens[1].replace( "{SHA}", "" ).trim() );
    }
  }

  public boolean isAuthorized( String authInfo ) {
    String credentials = new String(
        Base64.getDecoder().decode( authInfo ),
        Charset.forName( "UTF-8" )
    );

    String[] tokens = credentials.split( ":" );

    return verifyPassword( tokens[0], tokens[1] );
  }

  private boolean verifyPassword( String username, String password ) {
    if(this.passwords.containsKey( username )) {
      return this.passwords.get( username ).equals( this.encryptClearPassword( password ) );
    }

    return false;
  }

  private String encryptClearPassword( String password ) {
    try {
      MessageDigest mDigest = MessageDigest.getInstance( "SHA-1" );
      byte[] result = mDigest.digest( password.getBytes() );

      return Base64.getEncoder().encodeToString( result );
    } catch ( Exception e ) {
      return "";
    }
  }
}
