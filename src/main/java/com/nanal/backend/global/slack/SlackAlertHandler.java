package com.nanal.backend.global.slack;

import com.nanal.backend.domain.auth.event.RegisterEvent;
import com.nanal.backend.global.config.SchedulingConfig;
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
    private String token;
    @Value(value = "${slack.channel.monitor}")
    private String monitorChannel;

    @Value(value = "${slack.channel.info}")
    private String infoChannel;

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
            slack.methods(token).chatPostMessage(req -> req.channel(infoChannel).text(message));
        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT,
            classes = SchedulingConfig.SchedulingEvent.class
    )
    public void publishSchedulingEvent(SchedulingConfig.SchedulingEvent schedulingEvent){
        try{
            String message = "[Scheduling Info] \n" +
                    "Message : " + schedulingEvent.getMessage();

            Slack slack = Slack.getInstance();
            slack.methods(token).chatPostMessage(req -> req.channel(monitorChannel).text(message));

        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }
}
