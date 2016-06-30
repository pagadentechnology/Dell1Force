package id.tech.model;

/**
 * Created by macbook on 5/9/16.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PojoAbsence {
    @SerializedName("Status")
    @Expose
    private String Status;
    @SerializedName("Message")
    @Expose
    private String Message;

    /**
     *
     * @return
     * The Status
     */
    public String getStatus() {
        return Status;
    }

    /**
     *
     * @param Status
     * The Status
     */
    public void setStatus(String Status) {
        this.Status = Status;
    }

    /**
     *
     * @return
     * The Message
     */
    public String getMessage() {
        return Message;
    }

    /**
     *
     * @param Message
     * The Message
     */
    public void setMessage(String Message) {
        this.Message = Message;
    }

}
