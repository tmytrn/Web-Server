import java.io.File;
import java.io.OutputStream;

public class DeleteResponse extends Response {
  public DeleteResponse( Request request, Resource resource){
    super(request,  resource);
    this.setCode( 200 );
    this.setReasonPhrase( "OK" );
    File fileToDelete = new File(resource.getAbsolutePath());
  }

  @Override
  void send( OutputStream out ) {

  }

  private void deleteFile(File fileToDelete){
    if(fileToDelete.exists() && fileToDelete.isFile()){
      fileToDelete.delete();
    }
  }

}
