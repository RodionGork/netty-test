package nettytest;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

public class MyChannelHandler extends SimpleChannelHandler {

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Channel channel = e.getChannel();
        long millis = System.currentTimeMillis();
        if (millis % 5 != 0) {
            writeNormal(channel, millis);
        } else {
            returnError(ctx);
        }
        channel.close();
        super.messageReceived(ctx, e);
    }

    private void returnError(ChannelHandlerContext ctx) {
        ctx.getChannel().write(new DefaultHttpResponse(HttpVersion.HTTP_1_1,
                (Math.random() > 0.5) ? HttpResponseStatus.SERVICE_UNAVAILABLE : HttpResponseStatus.BAD_REQUEST));
        System.out.println("error occured");
    }

    private void writeNormal(Channel channel, long millis) {
        channel.write(String.format("CurrentMillis: %s%n", millis));
        System.out.println("normal response " + millis);
    }

}
