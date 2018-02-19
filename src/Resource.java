import java.io.*;
import java.util.*;

public class Resource {

  private HttpdConf configuration;
  private String fileURI;
  private File document;
  private String absolutePath;
  private String htAccessLocation;
  private boolean isScriptAlias;
  private boolean isAlias;
  private long lastModified;
  private Date lastModifiedDate;

  public Resource( String uri, HttpdConf conf ) {

    this.configuration = conf;
    this.fileURI = uri;
    this.isAlias = this.uriContains( this.configuration.getAliasMap() );
    this.isScriptAlias = this.uriContains( this.configuration.getScriptAliasMap() );
    this.absolutePath = absolutePath();
    this.document = new File( this.absolutePath );
    this.lastModified = this.document.lastModified();
    this.lastModifiedDate = new Date( this.lastModified );

  }

  public String absolutePath( ) {

    if ( isAliased() ) {
      modifyURI( this.configuration.getAliasMap() );
    } else if ( isScript() ) {
      modifyURI( this.configuration.getScriptAliasMap() );
    } else {
      this.addDocumentRootToTheStartOfURI();
    }

    if ( isDirectory() ) {
      this.appendDirectoryIndexToURI();
    }

    return this.absolutePath;

  }

  private boolean isAliased( ) {

    return this.isAlias;

  }

  private void addDocumentRootToTheStartOfURI( ) {

    this.absolutePath = configuration.lookupConfiguration( "DocumentRoot" ) + this.fileURI;

  }

  private String appendDirectoryIndexToURI( ) {

    this.absolutePath += configuration.lookupConfiguration( "DirectoryIndex" );

    return this.absolutePath;

  }

  private boolean isDirectory( ) {

    return new File( this.absolutePath ).isDirectory();

  }

  public boolean isScript( ) {

    return this.isScriptAlias;

  }

  public boolean isProtected( ) {

    this.htAccessLocation = this.getURIDirectoryTree() + "/.htaccess";
    File htAccess = new File( this.htAccessLocation );

    return htAccess.exists();

  }

  public String getURIDirectoryTree( ) {

    return this.absolutePath.substring( 0, this.absolutePath.lastIndexOf( '/' ) );

  }

  private boolean uriContains( HashMap<String, String> map ) {

    for ( String keyToCheck : map.keySet() ) {
      if ( this.fileURI.contains( keyToCheck ) ) {
        return true;
      }
    }

    return false;

  }

  private void modifyURI( HashMap<String, String> mapToBeChecked ) {

    for ( String alias : mapToBeChecked.keySet() ) {
      if ( this.fileURI.contains( alias ) ) {
        String replacement = mapToBeChecked.get( alias );
        this.absolutePath = this.fileURI.replace( alias, replacement );
      }
    }

  }

  public File getFile( ) {

    return this.document;

  }

  public String getAbsolutePath( ) {

    return this.absolutePath;

  }

  public String getHtAccessLocation( ) {

    return this.htAccessLocation;

  }

  public long getLastModified( ) {

    return this.lastModified;

  }

  public Date getLastModifiedDate( ) {

    return this.lastModifiedDate;

  }

}
