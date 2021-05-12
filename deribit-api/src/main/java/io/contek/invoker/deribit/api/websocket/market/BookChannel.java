package io.contek.invoker.deribit.api.websocket.market;

import io.contek.invoker.deribit.api.websocket.WebSocketChannel;
import io.contek.invoker.deribit.api.websocket.WebSocketChannelId;
import io.contek.invoker.deribit.api.websocket.common.WebSocketChannelMessage;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;

import static io.contek.invoker.deribit.api.websocket.common.constants.WebSocketChannelKeys._book;
import static java.lang.String.format;

@ThreadSafe
public final class BookChannel extends WebSocketChannel<BookChannel.Id, BookChannel.Message> {

  BookChannel(Id id) {
    super(id);
  }

  @Override
  protected Class<BookChannel.Message> getMessageType() {
    return BookChannel.Message.class;
  }

  @Immutable
  public static final class Id extends WebSocketChannelId<Message> {

    private Id(String value) {
      super(value);
    }

    public static Id of(String instrumentName, String group, int depth, String interval) {
      return new Id(format("%s.%s.%s.%d.%s", _book, instrumentName, group, depth, interval));
    }
  }

  @NotThreadSafe
  public static final class Message extends WebSocketChannelMessage<OrderBook> {}
}