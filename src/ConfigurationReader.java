import java.io.*;

public abstract class ConfigurationReader {
  private File file;

  public ConfigurationReader(String fileName){
    file = new File( fileName );
  }
}
