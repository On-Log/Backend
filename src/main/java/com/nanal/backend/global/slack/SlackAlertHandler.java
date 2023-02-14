package com.nanal.backend.global.slack;

import com.nanal.backend.domain.auth.event.RegisterEvent;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Slf4j
@Component
public class SlackAlertHandler {
    @Value(value = "${slack.token}")
    String token;
    @Value(value = "${slack.channel.monitor}")
    String channel;

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT,
            classes = RegisterEvent.class
    )
    public void publishRegisterEvent(RegisterEvent registerEvent){
        try{
            String message = "[Register Info] \n" +
                            "User Nickname : " + registerEvent.getNickname() + "\n" +
                            "User Email : " + registerEvent.getEmail();

            Slack slack = Slack.getInstance();
            slack.methods(token).chatPostMessage(req -> req.channel(channel).text(message));

        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }


}
