import java.io.File;
import java.io.FileInputStream;

public class Resource {
  private byte[] byteArray;
  HttpdConf configuration;
  String fileURI;
  File document;

  //convert the indexhtml to a byte array to throw into an output stream
  public Resource(String uri, HttpdConf conf){
    configuration = conf;
    fileURI = uri;
    try {
      document = new File( absolutePath() );
//      System.out.println(fileURI);
//      FileInputStream fileReader = new FileInputStream( document);
//      byteArray = new byte[(int)document.length()];
//      fileReader.read(byteArray);
//      System.out.println(new String(byteArray));
//      fileReader.close();
    }
    catch (Exception e){
      e.printStackTrace();
      //500 error
    }

  }

  public String absolutePath(){
    if(isAliased( fileURI )){
      fileURI = configuration.lookupAlias( fileURI );
    }
    else if(isScript(fileURI)){
      fileURI = configuration.lookupScript( fileURI );
    }
    else{
      fileURI = appendDocumentRoot( fileURI );
    }
    if(isFile( fileURI )){
      return fileURI;
    }
    else{
      fileURI= fileURI + configuration.lookup( "DirectoryIndex" );
      return fileURI;
    }
  }
  public boolean isFile(String uri){
    File fileCheck = new File( uri);
    return fileCheck.isFile();
  }
  public boolean isAliased(String uri){
    return (configuration.lookupAlias( uri ) != null);

  }

  public String appendDocumentRoot(String uri){
    return configuration.lookup( "DocumentRoot" ) + uri ;
  }

  public boolean isScript(String uri){
    return (configuration.lookupScript( uri ) != null);
  }

  public boolean isProtected(){
    File htAccess = new File("public_html/.htaccess");
    return (htAccess.exists());
  }

  public File getFile(){
    return this.document;
  }
}
