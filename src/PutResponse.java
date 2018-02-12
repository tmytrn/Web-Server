import java.io.File;
import java.io.FileOutputStream;

public class PutResponse extends Response {
  public PutResponse(Request request, Resource resource){
    super(request, resource);
    this.resource = resource;
    File filePath = new File(resource.fileURI);

    if(filePath.exists()){
      this.code = 200;
      this.reasonPhrase = "OK";
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
        fileOutputStream.write( request.getBody() );
        fileOutputStream.flush();
        fileOutputStream.close();
        this.code = 201;
        this.reasonPhrase = "Created";

      }catch(Exception e){
        e.printStackTrace();
      }

  }

}
