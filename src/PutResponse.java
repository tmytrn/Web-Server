import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class PutResponse extends Response {
  public PutResponse(Request request, Resource resource, MimeTypes mimeTypes){
    super(request, resource, mimeTypes);
    File filePath = new File(resource.absolutePath());
    createResource(filePath);
  }

  void send( OutputStream out ) {
    String response = this.createHeaders();
    try {
      String res = new String( response.getBytes() );
      System.out.println( res );
      out.write( response.getBytes() );
      out.flush();
    } catch ( Exception e ) {
      e.printStackTrace();
    }

  }

  public void createResource(File createFile){
      createFile.getParentFile().mkdirs();
      try {
        createFile.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream( createFile );
        fileOutputStream.write( this.getRequestBody() );
        fileOutputStream.flush();
        fileOutputStream.close();
        this.setCode(201);
        this.setReasonPhrase("Created");

      }catch(Exception e){
        e.printStackTrace();
      }

  }

}
