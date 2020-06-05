package tim2.auth.rabbitmq;

public class EmailRequestMessage {

    private String text;

    public EmailRequestMessage() {
    }

    public EmailRequestMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "EmailRequestMessage{" +
                "text='" + text + '\'' +
                '}';
    }
}
