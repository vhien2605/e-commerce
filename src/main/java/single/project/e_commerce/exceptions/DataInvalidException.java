package single.project.e_commerce.exceptions;

public class DataInvalidException extends RuntimeException{
    public DataInvalidException(String message){
        super(message);
    }
}
