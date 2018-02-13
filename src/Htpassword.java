import sun.tools.jconsole.HTMLPane;

import java.util.HashMap;

public class Htpassword {
  private HashMap<String, String> users = new HashMap<>(  );

  public Htpassword(){
    this.load();

  }

  public void load(){

  }
  public boolean isAuthorized(String username, String password ){
    return true;

  }

}
