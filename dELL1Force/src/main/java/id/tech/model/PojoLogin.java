package id.tech.model;

/**
 * Created by macbook on 5/9/16.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PojoLogin {
    @SerializedName("Status")
    @Expose
    private String Status;
    @SerializedName("Message")
    @Expose
    private String Message;
    @SerializedName("Data")
    @Expose
    private Data Data;

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

    /**
     *
     * @return
     * The Data
     */
    public Data getData() {
        return Data;
    }

    /**
     *
     * @param Data
     * The Data
     */
    public void setData(Data Data) {
        this.Data = Data;
    }

    public class Data {

        @SerializedName("Name")
        @Expose
        private String Name;
        @SerializedName("Token")
        @Expose
        private String Token;
        @SerializedName("Level")
        @Expose
        private String Level;

        /**
         *
         * @return
         * The Name
         */
        public String getName() {
            return Name;
        }

        /**
         *
         * @param Name
         * The Name
         */
        public void setName(String Name) {
            this.Name = Name;
        }

        /**
         *
         * @return
         * The Token
         */
        public String getToken() {
            return Token;
        }

        /**
         *
         * @param Token
         * The Token
         */
        public void setToken(String Token) {
            this.Token = Token;
        }

        /**
         *
         * @return
         * The Level
         */
        public String getLevel() {
            return Level;
        }

        /**
         *
         * @param Level
         * The Level
         */
        public void setLevel(String Level) {
            this.Level = Level;
        }

    }
}
