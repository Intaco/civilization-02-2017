package mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexandria.Application;
import com.hexandria.mechanics.events.logic.Move;
import com.hexandria.mechanics.events.service.Connect;
import com.hexandria.mechanics.events.service.Ping;
import com.hexandria.websocket.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

/**
 * Created by frozenfoot on 02.05.17.
 */
public class MapperTests {
	ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void testPing() throws Exception {
		Message message = objectMapper.readValue("{\"event\":\"EVENTS.SERVICE.PING\",\"payload\":\"testytest\"}",
				Message.class);
		assertThat(message.getClass()).isEqualTo(Ping.class);

	}

	@Test
	public void testMove() throws Exception {
		Message message = objectMapper.readValue(
				"{\"event\":\"EVENTS.LOGIC.MOVE\",\"payload\":{\"playerIndex\":1,\"squadIndex\":0,\"moveTo\":{\"x\":4,\"y\":6}}}",
				Message.class);
		assertThat(message.getClass()).isEqualTo(Move.class);
	}

	@Test
	public void testConnect() throws Exception {
		Message message = objectMapper.readValue("{\"event\":\"EVENTS.SERVICE.CONNECT\",\"payload\":\"testytest\"}",
				Message.class);
		assertThat(message.getClass()).isEqualTo(Connect.class);
	}
}
