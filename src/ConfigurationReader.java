import java.io.*;

public abstract class ConfigurationReader {

  private File file;
  private BufferedReader readFile;
  private String currentLine;

  public ConfigurationReader( String fileName ) {
    this.file = new File( fileName );

    try {
      this.readFile = new BufferedReader( new FileReader( this.file ) );
    } catch ( FileNotFoundException e ) {
      e.printStackTrace();
    }

    try {
      this.currentLine = this.readFile.readLine();
    } catch ( IOException e ) {
      e.printStackTrace();
    }

  }

  public File getFile( ) {
    return this.file;
  }

  public BufferedReader getReadFile( ) {
    return this.readFile;
  }

  public boolean hasMoreLines( ) {
    return this.currentLine != null;
  }

  public String getCurrentLine( ) {
    return this.currentLine;
  }

  public void setNextLine( ) {
    try {
      this.currentLine = this.readFile.readLine();
    } catch ( IOException e ) {
      e.printStackTrace();
    }
  }

  public abstract void load( );

}
