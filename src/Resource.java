import java.io.File;
import java.io.FileInputStream;

public class Resource {
  private byte[] byteArray;
  HttpdConf configuration;
  String fileURI;

  //convert the indexhtml to a byte array to throw into an output stream
  public Resource(String uri, HttpdConf conf){
    configuration = conf;
    fileURI = uri;
    try {
      File document = new File( absolutePath() );
      FileInputStream fileReader = new FileInputStream( document);
      byteArray = new byte[(int)document.length()];
      fileReader.read(byteArray);
      System.out.println(new String(byteArray));
      fileReader.close();
    }
    catch (Exception e){
      //File document = new File(configuration.lookup("DirectoryIndex"));
      //e.printStackTrace();
      //404 error
    }


  }

  public String absolutePath(){
    if(configuration.lookup( fileURI ) != null){
        return configuration.lookup( fileURI );
    }
    else {
        return configuration.lookup("DocumentRoot") + fileURI;
    }
  }

  public boolean isScript(){
    return false;
  }

  public boolean isProtected(){
    return false;
  }
}
