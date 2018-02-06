import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

public class Resource {
  private byte[] byteFile;
  //convert the indexhtml to a byte array to throw into an output stream
  public Resource(String uri, HttpdConf conf){
    try {
      File document = new File( uri );
      FileInputStream fileReader = new FileInputStream( document);
      byteFile = new byte[(int)document.length()];
      fileReader.read(byteFile);
      //System.out.println(new String(byteFile));
      fileReader.close();
    }
    catch (Exception e){
      e.printStackTrace();
      //404 error
    }
  }

  public String absolutePath(){
    return "";
  }

  public boolean isScript(){
    return false;
  }

  public boolean isProtected(){
    return false;
  }
}
