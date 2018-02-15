import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

public class Worker implements Runnable {
  private Socket client;
  private MimeTypes mimes;
  private HttpdConf config;

  public Worker( Socket client, HttpdConf config, MimeTypes mimes ) {
    this.client = client;
    this.mimes = mimes;
    this.config = config;
  }

  public void run( ) {
    Request request;
    Resource resource;

    try {
      request = new Request( this.client.getInputStream() );
      resource = new Resource( request.getUri(), this.config );
      ResponseFactory responseMaker = new ResponseFactory(request, resource, this.mimes);
      Response response = responseMaker.getResponse( request, resource);
      response.send(client.getOutputStream());

//      System.out.println( "resource and request made" );

      //create response
      //send response back to stream

      this.client.close();
    } catch ( IOException e ) {
      e.printStackTrace();
    }
  }
}
