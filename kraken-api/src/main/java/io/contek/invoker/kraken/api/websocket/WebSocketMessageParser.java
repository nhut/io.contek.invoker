package io.contek.invoker.kraken.api.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.contek.invoker.commons.websocket.IWebSocketMessageParser;
import io.contek.invoker.kraken.api.websocket.common.WebSocketInboundMessage;
import io.contek.invoker.kraken.api.websocket.common.WebSocketResponse;


import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import static io.contek.invoker.kraken.api.websocket.market.OrderBookChannel.toOrderBook;
import static io.contek.invoker.kraken.api.websocket.market.TradesChannel.toTrades;

@Immutable
final class WebSocketMessageParser implements IWebSocketMessageParser {

  private final Gson gson = new Gson();

  static WebSocketMessageParser getInstance() {
    return InstanceHolder.INSTANCE;
  }

  @Nullable
  @Override
  public WebSocketInboundMessage parse(String text) {
    JsonElement json = gson.fromJson(text, JsonElement.class);

    if (json.isJsonArray()) {
      return toDataMessage(json.getAsJsonArray());
    } else if (json.isJsonObject()) {
      return toConfirmationMessage(json.getAsJsonObject());
    } else {
      throw new IllegalArgumentException(text);
    }
  }

  private WebSocketInboundMessage toConfirmationMessage(JsonObject obj) {
    return gson.fromJson(obj, WebSocketResponse.class);
  }

  private WebSocketInboundMessage toDataMessage(JsonArray obj) {
    String type = obj.get(2).getAsString();
    if (type.equals("trade")) {
      return toTrades(obj);
    } else if (type.startsWith("book")) {
      return toOrderBook(obj);
    } else {
      throw new RuntimeException(String.format("Invalid type %s", type));
    }
  }


  private WebSocketMessageParser() {
  }

  @Immutable
  private static final class InstanceHolder {

    private static final WebSocketMessageParser INSTANCE = new WebSocketMessageParser();

    private InstanceHolder() {
    }
  }
}
