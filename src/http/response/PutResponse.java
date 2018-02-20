package http.response;

import http.request.Request;
import http.resource.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class PutResponse extends Response {

  public PutResponse( Request request, Resource resource ) {

    super( request, resource );
    File filePath = new File( resource.absolutePath() );
    this.createResource( filePath );
    this.addContentLocationHeader();

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

  public void createResource( File createFile ) {

    createFile.getParentFile().mkdirs();

    try {
      createFile.createNewFile();
      FileOutputStream fileOutputStream = new FileOutputStream( createFile );
      fileOutputStream.write( this.getRequestBody() );
      fileOutputStream.flush();
      fileOutputStream.close();
      this.setCode( 201 );
      this.setReasonPhrase( "Created" );

    } catch ( Exception e ) {
      e.printStackTrace();
    }

  }

  private void addContentLocationHeader( ) {

    String absolutePath = this.getResource().getAbsolutePath();
    String[] pathSplit = absolutePath.split( "/" );
    this.getResponseHeaders().put( "Content-Location", "/" + pathSplit[pathSplit.length - 1] );

  }

}
