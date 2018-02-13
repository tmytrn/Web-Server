import java.util.*;

public class Htaccess extends ConfigurationReader{
  private HashMap<String, String> users;
  private Htpassword userFile;
  private String authType;
  private String authName;
  private String require;

  public Htaccess(){
    super(null);
    userFile = new Htpassword();

  }
  public void load(){

  }
  public boolean isAuthorized(String username, String password){
    userFile.isAuthorized( username, password );
    return true;
  }


}
