import java.io.*;
import java.util.*;

public class Resource {
  private byte[] byteArray;
  private HttpdConf configuration;
  private String fileURI;
  private File document;

  public Resource( String uri, HttpdConf conf ) {
    this.configuration = conf;
    this.fileURI = uri;

    try {
//      File document = new File( absolutePath() );
//      FileInputStream fileReader = new FileInputStream( document);
//      byteArray = new byte[(int)document.length()];
//      fileReader.read(byteArray);
//      System.out.println("does ht access exist: " +isProtected());
//      System.out.println(new String(byteArray));
//      fileReader.close();
    } catch ( Exception e ) {
      e.printStackTrace();
      //500 error
    }

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

    return this.fileURI;

  }

  private boolean isAliased( ) {
    return this.uriContains( this.configuration.getAliasMap() );
  }

  private String addDocumentRootToTheStartOfURI( ) {
    this.fileURI = configuration.lookupConfiguration( "DocumentRoot" ) + this.fileURI;
    return this.fileURI;
  }

  private String appendDirectoryIndexToURI( ) {
    this.fileURI += configuration.lookupConfiguration( "DirectoryIndex" );
    return this.fileURI;
  }

  private boolean isFile( ) {
    File fileCheck = new File( this.fileURI );
    return fileCheck.isFile();
  }

  public boolean isScript( ) {
    return this.uriContains( this.configuration.getScriptAliasMap() );
  }

  public boolean isProtected( ) {
    File htAccess = new File( "/public_html/.htaccess" );
    return ( htAccess.exists() );
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
        this.fileURI = this.fileURI.replace( alias, replacement );
        System.out.println( "The modified fileURI is : " + this.fileURI );
      }
    }
  }

  public File getFile( ) {
    return this.document;
  }

}
