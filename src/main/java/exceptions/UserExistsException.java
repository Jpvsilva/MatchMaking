package exceptions;

public class UserExistsException extends Exception {

    public UserExistsException(){
        super();
    }

    public UserExistsException(String m){
        super(m);
    }
}
