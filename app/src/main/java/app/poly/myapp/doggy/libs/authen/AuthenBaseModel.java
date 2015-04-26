package app.poly.myapp.doggy.libs.authen;


public class AuthenBaseModel {

    private String username;
    private String password;
    private String repassword;
    private int roleid;

    public AuthenBaseModel(){
        this("","");
    }

    public AuthenBaseModel(String username, String password){
        this(username, password, "");
    }

    public AuthenBaseModel(String username, String password, String repassword){
        this(username,password,repassword,0);
    }

    public AuthenBaseModel(String username, String password, String repassword, int roleid){
        this.username = username;
        this.password = password;
        this.repassword = repassword;
        this.roleid = roleid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }
}
