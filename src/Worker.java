import sun.rmi.log.ReliableLog;

import java.io.File;
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

    try {
      Request request = new Request( this.client.getInputStream() );
      Resource resource = new Resource( request.getUri(), this.config );
      ResponseFactory responseMaker = new ResponseFactory();
      Response response = responseMaker.getResponse( request, resource, this.mimes );
      response.send( this.client.getOutputStream() );
      Logger log = new Logger( this.config.lookupConfiguration( "LogFile" ) );
      String IPAddress = this.client.getInetAddress().getLocalHost().getHostAddress();
      log.write( request, response, IPAddress );
      this.client.close();
    } catch ( Exception e ) {
      e.printStackTrace();
      try {
        throw new BadRequest( "Bad Request" );
      } catch ( BadRequest badRequest ) {
        badRequest.printStackTrace();
      }
    }
  }
}
