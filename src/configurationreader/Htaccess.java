package configurationreader;

import java.util.*;

public class Htaccess extends ConfigurationReader {

  private Htpassword userFile;
  private String authType;
  private String authName;
  private String require;
  private HashMap<String, String> htAccessInfo;

  public Htaccess( String filename ) {

    super( filename );
    this.htAccessInfo = new HashMap<>();
    this.load();

  }

  public void load( ) {

    this.parseHtAccessFile();
    this.userFile = new Htpassword( this.htAccessInfo.get( "AuthUserFile" ) );
    this.authType = this.htAccessInfo.get( "AuthType" );
    this.authName = this.htAccessInfo.get( "AuthName" );
    this.require = this.htAccessInfo.get( "Require" );

  }

  public void parseHtAccessFile( ) {

    String htAccessLine;
    String[] htAccessLineSplit;

    while ( this.hasMoreLines() ) {
      htAccessLine = this.getCurrentLine();

      if ( htAccessLine.contains( "\"" ) ) {
        htAccessLine = htAccessLine.replaceAll( "\"", "" );
        htAccessLineSplit = htAccessLine.split( " ", 2 );
      } else {
        htAccessLineSplit = htAccessLine.split( " " );
      }

      this.htAccessInfo.put( htAccessLineSplit[0], htAccessLineSplit[1] );
      this.setNextLine();
    }

  }

  public boolean isAuthorized( String authorizationInformation ) {

    return this.userFile.isAuthorized( authorizationInformation );

  }


}
