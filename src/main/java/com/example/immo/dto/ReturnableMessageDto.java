import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ReturnableMessageDto {
    private Long messageId;
    private ReturnableRentalDto rental;
    private ReturnableUserDto user;
    private String message;

    public ReturnableMessageDto(Message message) {
        super();
        this.messageId = message.getMessageId();
        this.message = message.getMessage();
        this.user = new ReturnableUserDto(message.getUser());
        this.rental = new ReturnableRentalDto(message.getRental());
    }
}