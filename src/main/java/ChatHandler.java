import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.text.MessageFormat;
import java.time.LocalDate;

public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final ChannelGroup clients =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
            TextWebSocketFrame textWebSocketFrame) throws Exception {
        String messageContent = textWebSocketFrame.text();
        System.out.println(MessageFormat.format("Message received {0}", messageContent));

        for (Channel channel : clients) {
            channel.writeAndFlush(new TextWebSocketFrame(
                    String.format("[Server receives message at %s , message is %s", LocalDate
                            .now(), messageContent)));
        }
    }

    /**
     * when client connects, get client's channel and put in channel group
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.printf("client disconnects, channel long id: %s%n",
                          ctx.channel().id().asShortText());
        System.out.printf("client disconnects, channel short id: %s%n",
                          ctx.channel().id().asShortText());
        clients.remove(ctx.channel());
    }
}
