import java.io.File;
import java.io.OutputStream;

public class DeleteResponse extends Response {
  public DeleteResponse( Request request, Resource resource, MimeTypes mimeTypes ) {
    super( request, resource, mimeTypes );
    File fileToDelete = new File(resource.getAbsolutePath());
    deleteFile( fileToDelete );
  }

  @Override
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

  private void deleteFile( File fileToDelete ) {
    if ( fileToDelete.exists() && fileToDelete.isFile() ) {
      fileToDelete.delete();
      this.setCode( 204 );
      this.setReasonPhrase( "No Content" );
    }
  }

}
