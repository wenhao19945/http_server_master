package com.example.httpserver.server;

import com.example.httpserver.handler.NettyHttpServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.ResourceLeakDetector;
import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author WenHao
 * @ClassName NettyHttpServer
 * @date 2022/7/7 17:17
 * @Description
 */
public class NettyHttpServer{

  private static final Logger logger = LoggerFactory.getLogger(NettyHttpServer.class);

  private int port;

  /**
   * netty服务
   */
  private final ServerBootstrap serverBootstrap;

  /**
   * Selector线程池
   */
  private final EventLoopGroup eventLoopGroupSelector;

  /**
   * boss线程池
   */
  private final EventLoopGroup eventLoopGroupBoss;

  public NettyHttpServer(int port) {
    this.port = port;

    this.serverBootstrap = new ServerBootstrap();

    this.eventLoopGroupBoss = new NioEventLoopGroup(1, new ThreadFactory() {
      private AtomicInteger threadIndex = new AtomicInteger(0);

      @Override
      public Thread newThread(Runnable r) {
        return new Thread(r, String.format("HttpServerBoss_%d", this.threadIndex.incrementAndGet()));
      }
    });

    this.eventLoopGroupSelector = new NioEventLoopGroup(4, new ThreadFactory() {
      private AtomicInteger threadIndex = new AtomicInteger(0);
      private int threadTotal = 3;
      @Override
      public Thread newThread(Runnable r) {
        return new Thread(r, String.format("HttpServerSelector_%d_%d", threadTotal, this.threadIndex.incrementAndGet()));
      }
    });

  }

  public void start() {

    // 1. 绑定两个线程组分别用来处理客户端通道的accept和读写时间
    this.serverBootstrap.group(this.eventLoopGroupBoss, this.eventLoopGroupSelector)
        // 2. 绑定服务端通道NioServerSocketChannel
        .channel(NioServerSocketChannel.class)
        //封装IP和端口信息，常用于Socket通信
        .localAddress(new InetSocketAddress(this.port))
        // 3. 给读写事件的线程通道绑定handler去真正处理读写
        // ChannelInitializer初始化通道SocketChannel
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel socketChannel) throws Exception {
            // 请求解码器
            socketChannel.pipeline().addLast("http-decoder", new HttpRequestDecoder());
            // 将HTTP消息的多个部分合成一条完整的HTTP消息
            socketChannel.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65535));
            // 响应转码器
            socketChannel.pipeline().addLast("http-encoder", new HttpResponseEncoder());
            // 解决大码流的问题，ChunkedWriteHandler：向客户端发送HTML5文件
            socketChannel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
            // 自定义处理handler
            socketChannel.pipeline().addLast("http-server", new NettyHttpServerHandler());
          }
        });


    //内存泄漏检测 开发推荐PARANOID 线上SIMPLE
    ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.SIMPLE);
    try {
      // 4. 监听端口（服务器host和port端口），同步返回
      ChannelFuture future = this.serverBootstrap.bind().sync();
      if (future.isSuccess()) {
        logger.info("netty http server start up on port : {}", this.port);
      }
    } catch (InterruptedException e1) {
      throw new RuntimeException("this.serverBootstrap.bind().sync() InterruptedException", e1);
    }
    // 当通道关闭时继续向后执行，这是一个阻塞方法
    //future.channel().closeFuture().sync();

  }

  public void shutdown() {
    try {
      // 异步shutdownGracefully().syncUninterruptibly()
      this.eventLoopGroupBoss.shutdownGracefully();
      this.eventLoopGroupSelector.shutdownGracefully();
    } catch (Exception e) {
      logger.error("NettyRemotingServer shutdown exception, {}", e);
    }
  }

}
