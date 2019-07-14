package pt.ubi.trackingwebapp;

public class Message {

    private String messageBody;
    private String messageOwner;
    private Boolean sendByUs;

   public Message(String messageOwner, String messageBody, Boolean sendByUs) {
        this.messageOwner = messageOwner;
        this.messageBody = messageBody;
        this.sendByUs = sendByUs;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getMessageOwner() {
        return messageOwner;
    }

    public void setMessageOwner(String messageOwner) {
        this.messageOwner = messageOwner;
    }

    public Boolean getSendByUs() {
        return sendByUs;
    }

    public void setSendByUs(Boolean sendByUs) {
        this.sendByUs = sendByUs;
    }
}
