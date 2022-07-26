package com.example.httpserver.handler;

import com.alibaba.fastjson2.JSON;
import com.example.httpserver.ApplicationData;
import com.example.httpserver.annotation.RequestApi;
import com.example.httpserver.util.FileUtil;
import com.example.httpserver.util.ParameterUtil;
import com.example.httpserver.util.ResponseUtil;
import com.example.httpserver.util.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义Handler.
 *
 * @author WenHao
 * @date 2022/7/7 17:47
 */
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

  private static final Logger logger = LoggerFactory.getLogger(NettyHttpServerHandler.class);

  /**
   * received message
   */
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
    FullHttpResponse response = null;
    // /index/xxx  -> ["index","xxx"]
    String[] url = StringUtil.getRequestPath(request.uri());
    // 100 continue
    if (HttpUtil.is100ContinueExpected(request)) {
      logger.info("100-continue");
      ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
    }else{
      // path is null?
      if(StringUtil.isBlank(url[0])){
        response = ResponseUtil.responseText(HttpResponseStatus.OK, "welcome！");
      }else{
        String key = "/".concat(url[0]);
        Map<String, String> classPath = getClassPath(ApplicationData.ACTION, key);
        // this is a action ?
        if(null != classPath){
          String value = "/".concat(url[1]);
          // verify class path
          Class<?> clazz = Class.forName(classPath.get(key));
          // get declared methods
          Method[] methods = clazz.getDeclaredMethods();
          for(Method method : methods){
            RequestApi mapping = method.getAnnotation(RequestApi.class);
            // skip not @RequestApi the method
            if(null == mapping){
              continue;
            }
            //do method
            if(request.method().name().equals(mapping.method().toString()) && value.equals(mapping.value())){
              //TODO: update request and response for before or after
              logger.info("<<<<< request: {} - {} - {}",
                  request.method().name(),
                  key + value,
                  JSON.toJSONString(ParameterUtil.getParam(request)));
              response = (DefaultFullHttpResponse)method.invoke(clazz.newInstance(), request, response);
              logger.info(">>>>> response: {} ", new String(ByteBufUtil.getBytes(response.content()), CharsetUtil.UTF_8));
            }
          }
        }else{
          // get resources TODO: other static files
          String favicon = "favicon.ico";
          FileUtil fileUtil = new FileUtil();
          byte[] ico = fileUtil.loadIco(favicon);
          ByteBuf buf =  Unpooled.buffer(ico.length);
          buf.writeBytes(ico);
          response = ResponseUtil.responseIco(buf);
        }
      }
      // not found
      if (null == response) {
        response = ResponseUtil.responseText(HttpResponseStatus.NOT_FOUND, HttpResponseStatus.NOT_FOUND.reasonPhrase());
      }
      // channel write response
      ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    super.exceptionCaught(ctx, cause);
    logger.error("connection exception..... {}", cause.getMessage());
    cause.printStackTrace();
    FullHttpResponse response = ResponseUtil
        .responseText(HttpResponseStatus.INTERNAL_SERVER_ERROR, HttpResponseStatus.INTERNAL_SERVER_ERROR.reasonPhrase());
    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
  }

  /**
   * 通道建立成功了
   */
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    logger.info("----- channel active client online " + ctx.channel().remoteAddress());
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    logger.info("----- channel inactive " + ctx.channel().remoteAddress());
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    super.channelReadComplete(ctx);
    logger.info("----- read complete " + ctx.channel().remoteAddress());
  }

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    super.handlerAdded(ctx);
    logger.info("----- channel add " + ctx.channel().remoteAddress());
  }

  /**
   * 通道被移除
   */
  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    super.handlerRemoved(ctx);
    logger.info("----- channel removed " + ctx.channel().remoteAddress());
  }

  /**
   * get class path by key
   */
  private Map<String, String> getClassPath(List<Map<String, String>> list, String key){
    for(Map<String, String> map : list){
      if(map.containsKey(key)){
        return map;
      }
    }
    return null;
  }

}
