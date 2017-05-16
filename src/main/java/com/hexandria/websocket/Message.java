package com.hexandria.websocket;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.hexandria.mechanics.events.game.Start;
import com.hexandria.mechanics.events.logic.Create;
import com.hexandria.mechanics.events.logic.Delete;
import com.hexandria.mechanics.events.logic.Move;
import com.hexandria.mechanics.events.logic.Update;
import com.hexandria.mechanics.events.service.Connect;
import com.hexandria.mechanics.events.service.Ping;

/**''
 * Created by root on 09.04.17.
 */


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "event")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Move.class, name = "EVENTS.LOGIC.MOVE"),
        @JsonSubTypes.Type(value = Connect.class, name = "EVENTS.SERVICE.CONNECT"),
        @JsonSubTypes.Type(value = Ping.class, name = "EVENTS.SERVICE.PING"),
        @JsonSubTypes.Type(value = Update.class, name = "EVENTS.LOGIC.UPDATE"),
        @JsonSubTypes.Type(value = Delete.class, name = "EVENTS.LOGIC.DELETE"),
        @JsonSubTypes.Type(value = Start.class, name = "EVENTS.GAME.START"),
        @JsonSubTypes.Type(value = Create.class, name = "EVENTS.LOGIC.CREATE")
})
public abstract class Message {
}
