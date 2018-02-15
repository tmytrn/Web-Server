import java.io.File;
import java.io.FileOutputStream;

public class PutResponse extends Response {
  public PutResponse(Request request, Resource resource){
    super(request, resource);
    File filePath = new File(resource.absolutePath());
    if(filePath.exists()){
      this.setCode(200);
      this.setReasonPhrase("OK");
    }
    else{
      createResource(filePath);
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
