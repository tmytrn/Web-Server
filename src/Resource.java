import java.io.*;
import java.util.*;

public class Resource {

  private HttpdConf configuration;
  private String fileURI;
  private File document;
  private String absolutePath;

  public Resource( String uri, HttpdConf conf ) {
    this.configuration = conf;
    this.fileURI = uri;
    this.absolutePath = absolutePath();
    this.document = new File( this.absolutePath );
  }

  public String absolutePath( ) {
    if ( isAliased() ) {
      modifyURI( this.configuration.getAliasMap() );
    } else if ( isScript() ) {
      modifyURI( this.configuration.getScriptAliasMap() );
    } else {
      this.addDocumentRootToTheStartOfURI();
    }

    if ( !isFile() ) {
      this.appendDirectoryIndexToURI();
    }

    return this.absolutePath;

  }

  private boolean isAliased( ) {
    return this.uriContains( this.configuration.getAliasMap() );
  }

  private void addDocumentRootToTheStartOfURI( ) {
    this.absolutePath = configuration.lookupConfiguration( "DocumentRoot" ) + this.fileURI;
  }

  private String appendDirectoryIndexToURI( ) {
    this.absolutePath += configuration.lookupConfiguration( "DirectoryIndex" );
    return this.absolutePath;
  }

  private boolean isFile( ) {
    return new File( this.absolutePath ).isFile();
  }

  public boolean isScript( ) {
    return this.uriContains( this.configuration.getScriptAliasMap() );
  }

  public boolean isProtected( ) {
    File htAccess = new File( this.getURIDirectoryTree() + "/.htaccess" );
    return htAccess.exists();

  }

  public String getURIDirectoryTree( ) {
    return this.absolutePath.substring( 0, this.absolutePath.lastIndexOf( "/" ) );
  }

  private boolean uriContains( HashMap<String, String> map ) {
    for ( String keyToCheck : map.keySet() ) {
      if ( this.fileURI.contains( keyToCheck ) ) {
        System.out.println( "URI contains key : " + keyToCheck );
        return true;
      }
    }

    return false;
  }

  private void modifyURI( HashMap<String, String> map ) {
    for ( String alias : map.keySet() ) {
      if ( this.fileURI.contains( alias ) ) {
        String replacement = this.configuration.lookupAlias( alias );
        this.absolutePath = this.fileURI.replace( alias, replacement );
        System.out.println( "The modified fileURI is : " + this.fileURI );
      }
    }
  }

  public File getFile( ) {
    return this.document;
  }

  public String getAbsolutePath( ) {
    return this.absolutePath;
  }

}
