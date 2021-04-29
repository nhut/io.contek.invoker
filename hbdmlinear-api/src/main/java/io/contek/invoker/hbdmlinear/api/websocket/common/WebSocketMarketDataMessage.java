package io.contek.invoker.hbdmlinear.api.websocket.common;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public abstract class WebSocketMarketDataMessage<T> extends WebSocketInboundMessage {

  public String ch;
  public Long ts;
}
