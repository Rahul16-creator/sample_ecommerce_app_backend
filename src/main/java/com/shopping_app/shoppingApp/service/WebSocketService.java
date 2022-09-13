package com.shopping_app.shoppingApp.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class WebSocketService {
  private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketService.class);


  private String virtualPrefix="/topic"; // for forwarding topics to queue on ActiveMQ UI

  // TOPICS
  public static final String GET_PRODUCT_COUNT_TOPIC = "/get_product_count";

  private final SimpMessagingTemplate webSocket;

  private void publish(String destination, Object payload) {
    try {
      webSocket.convertAndSend(destination, payload);
    } catch (MessagingException e) {
      LOGGER.error(
          "Encountered error sending websocket message to path {}, error: {}", destination, e);
    }
  }

  void publishProductCount(int count) {
    String destination = String.format(virtualPrefix + GET_PRODUCT_COUNT_TOPIC);
    LOGGER.info("Publishing get product count result to: {}", destination);
    publish(destination, count);
  }

}
