public class OperationResult {
    private final boolean success;
    private final String reason;

    private OperationResult(boolean success,String reason){
        this.success=success;
        this.reason=reason;
    }

    public static OperationResult ok(){
        return new OperationResult(true,"");
    }

    public static OperationResult fail(String reason){

        if (reason==null || reason.trim().isEmpty())
            reason = "The operation failed";

        return new OperationResult(false,reason);

    }

    public boolean isSuccess() {
        return success;
    }

    public String getReason() {
        return reason;
    }
}