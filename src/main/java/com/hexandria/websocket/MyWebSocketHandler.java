package com.hexandria.websocket;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexandria.mechanics.events.game.Turn;
import com.hexandria.mechanics.events.logic.Move;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

/**
 * Created by root on 25.04.17.
 */
@SuppressWarnings("OverlyBroadThrowsClause")
public class MyWebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyWebSocketHandler.class);
    @NotNull
    private final RemotePointService service;

    @Autowired
    private final ObjectMapper objectMapper;

    public MyWebSocketHandler(@NotNull RemotePointService service, ObjectMapper objectMapper){
        this.service = service;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        LOGGER.warn("Transportation problem", throwable);
    }

    @SuppressWarnings("OverlyBroadThrowsClause")
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOGGER.info("Disconnected user with id  " + session.getAttributes().get("userId"));
        service.disconnectedHandler(new Long(session.getAttributes().get("userId").toString()));
        }

    @SuppressWarnings("OverlyBroadThrowsClause")
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        service.registerUser(new Long(session.getAttributes().get("userId").toString()), session);
        LOGGER.info("Connected user with id  " + session.getAttributes().get("userId"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage jsonTextMessage) throws Exception {
        final Long userId = new Long((Integer) session.getAttributes().get("userId"));
        try {
            final Message message = objectMapper.readValue(jsonTextMessage.getPayload(), Message.class);
            if(message.getClass() == Move.class || message.getClass() == Turn.class) {
                service.handleGameMessage(message, userId);
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            LOGGER.error("wrong json format response", ex);
        }
    }
}
