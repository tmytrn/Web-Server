import java.io.OutputStream;

public abstract class Response {
  public int code;
  public String reasonPhrase;
  public Resource resource;
  public Request request;
  public Response( Request request, Resource resource){


  }
  public void send( OutputStream out){

  }
  public String createHeaders(){
    StringBuilder headers = new StringBuilder(  );
    return null;

  }
  public String topLine(){
    return request.getHttpVersion() + " " +  this.code + " " + this.reasonPhrase + "\r\n";
  }

  /**
   * general form of a response is
   * HTTP_VERSION STATUS_CODE REASON_PHRASE
   * HTTP_HEADERS
   *
   * BODY
   *
   *
   */

}
