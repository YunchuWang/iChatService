import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WSServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        final ChannelPipeline pipeline = socketChannel.pipeline();

        // websocket 基于http， 需要编解码
        pipeline.addLast(new HttpServerCodec());
        // support for writing stream of big data
        pipeline.addLast(new ChunkedWriteHandler());
        // aggregate httpmessage into httprequest or responses
        // mostly in netty use this
        pipeline.addLast(new HttpObjectAggregator((1024*64)));

        // websocket protocol used to specify route client should connect to: /ws
        // responsible for handshaking (close, ping, pong) ping + pong = heartbeat
        // websocket transfer info in frames, different types of frames for different data types
        // TODO: research
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        pipeline.addLast(new ChatHandler());
    }
}
