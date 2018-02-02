import java.util.*;

public class MimeTypes extends ConfigurationReader{

  private HashMap<String,String> types;

  public MimeTypes(String fileName){
    super(fileName);
    this.types = new HashMap<>();
  }

  public HashMap<String, String> getTypes() {
    return this.types;
  }

  public boolean startsWithHashSymbol(String stringToBeChecked){
    return stringToBeChecked.startsWith( "#" );
  }

  public boolean isEmptyString(String stringToBeChecked){
    return stringToBeChecked.equals( "" );
  }

  public void skipUselessLines(){
    while( startsWithHashSymbol( this.getCurrentLine() ) || isEmptyString( this.getCurrentLine() )){
      this.setNextLine();
    }
  }

  public void load( ) {
    StringTokenizer tokens;

    while(this.hasMoreLines()){
      this.skipUselessLines();

      tokens = new StringTokenizer( this.getCurrentLine() );
      if(tokens.countTokens() > 1) {
        String mimeType = tokens.nextToken();
        System.out.println( mimeType );
        tokens.nextToken();

//        while( tokens.hasMoreTokens()) {
//          System.out.println( tokens.nextToken() );
//
//        }
      }


      this.setNextLine(); // move to next line to check
    }
  }
}