import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {
  private File logFile;
  private FileWriter fileWriter;
  private BufferedWriter bufferedWriter;


  public Logger(String fileName){
    logFile = new File(fileName);
    if(!logFile.exists()){
      logFile.getParentFile().mkdir();
      try{
        logFile.createNewFile();
      }catch(Exception e){
        //500
      }
    }
  }
  public void write(Request request, Response response, String IPAddress){
      try {
        fileWriter = new FileWriter( logFile , true);
        bufferedWriter = new BufferedWriter( fileWriter );
        String message = getLogMessage(request, response, IPAddress);
        bufferedWriter.append( message );
        bufferedWriter.close();
      }
      catch(Exception e){

      }

  }
  private String getLogMessage(Request request, Response response, String IPAddress){
    StringBuilder messageBuilder = new StringBuilder(  );
    messageBuilder.append( IPAddress );
    messageBuilder.append(" - ");
    messageBuilder.append(getUser(request));
    messageBuilder.append( " [" + formatDate(response.getCalendar()) + "] " );
    messageBuilder.append( "\"" +request.getFirstLineofRequest()+ "\" ");
    messageBuilder.append(getContentLength(response) + "\n");
    return messageBuilder.toString();
  }
  private String formatDate(Calendar calendar){
    SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MMM/yyyy:HH:mm:ss Z" );
    return dateFormat.format( calendar.getTime() );
  }
  private String getUser(Request request){
    if(request.lookup( "Authorization: " ) != null){
      return request.lookup( "Authorization: " );
    }
    return "-";
  }
  private String getContentLength(Response response){
    if( response.getContentLength() > 0){
      return  Integer.toString(response.getContentLength());
    }
    else{
      return "-";
    }
  }
}
