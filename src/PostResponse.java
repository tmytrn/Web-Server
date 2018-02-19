import java.io.*;

public class PostResponse extends Response {

  public PostResponse(Request request, Resource resource){
    super(request, resource);
    this.setCode(200);
    this.setReasonPhrase("OK" );
  }

  public void send( OutputStream out ) {
    String response = this.createHeaders();
    try {
      this.appendToFile();
      out.write( response.getBytes() );
      out.flush();
      out.close();
    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }

  public void appendToFile(){
    try {
      FileOutputStream fileOutputStream = new FileOutputStream( this.getResource().getFile() , true );
      fileOutputStream.write( this.getRequestBody() );
      fileOutputStream.flush();
      fileOutputStream.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }

  }

}
