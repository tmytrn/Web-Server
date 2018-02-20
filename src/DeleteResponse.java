import java.io.File;
import java.io.OutputStream;

public class DeleteResponse extends Response {

  public DeleteResponse( Request request, Resource resource ) {
    super( request, resource );
    File fileToDelete = new File( resource.getAbsolutePath() );
    deleteFile( fileToDelete );
  }

  public void send( OutputStream out ) {
    String response = this.createHeaders();

    try {
      out.write( response.getBytes() );
      out.flush();
    } catch ( Exception e ) {
      e.printStackTrace();
    }

  }

  private void deleteFile( File fileToDelete ) {
    if ( fileToDelete.exists() && fileToDelete.isFile() ) {
      fileToDelete.delete();
      this.setCode( 204 );
      this.setReasonPhrase( "No Content" );
    }
  }

}
