public class Response {
  private int code;
  private String reasonPhrase;
  private Resource resource;
  public Response(Resource resource){

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
