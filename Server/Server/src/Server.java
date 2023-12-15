import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


public class Server {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private static final int PORT = 12345;
    //为连接者分配id
    private AtomicInteger clientIdCounter = new AtomicInteger();
    //连接计数
    private AtomicInteger activeConnections = new AtomicInteger();

    public Server() throws Exception {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(PORT));
        serverSocketChannel.configureBlocking(false);
        // 注册到选择器，等待连接
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void start() throws Exception {
        System.out.println("Server started...");

        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();

                if (key.isAcceptable()) {
                    // 接受客户端连接
                    accept(key);
                } else if (key.isReadable()) {
                    // 读取客户端数据
                    read(key);
                }

                iter.remove();
            }
        }
    }

    private void accept(SelectionKey key) throws Exception {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);

        // 为新客户端分配唯一的ID
        int clientId = clientIdCounter.getAndIncrement();

        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(clientId);
        buffer.flip();

        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }

        channel.register(selector, SelectionKey.OP_READ);
        activeConnections.incrementAndGet();
        System.out.println("New connection from " + channel.getRemoteAddress() + " with client ID " + clientId);
    }

    private void read(SelectionKey key) throws Exception {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(10000);

        try {
            int numRead = channel.read(buffer);

            if (numRead == -1) {
                // 客户端关闭连接
                handleClientDisconnect(key, channel);
                return;
            }

            // 数据转发逻辑
            byte[] data = new byte[numRead];
            System.arraycopy(buffer.array(), 0, data, 0, numRead);
            broadcast(data, channel);
            System.out.println("Received message from " + channel.getRemoteAddress() + ": " + new String(data, StandardCharsets.UTF_8));
        } catch (IOException e) {
            // 客户端异常关闭
            handleClientDisconnect(key, channel);
        }
    }

    private void handleClientDisconnect(SelectionKey key, SocketChannel channel) throws IOException {
        key.cancel();
        channel.close();
        activeConnections.decrementAndGet();
        if (activeConnections.get() == 0) {
            System.out.println("All clients disconnected");
            clientIdCounter.set(0);
        }
        System.out.println("Client disconnected");
    }

    private void broadcast(byte[] data, SocketChannel origin) throws Exception {
        String messageWithDelimiter = new String(data, StandardCharsets.UTF_8);
        byte[] dataWithDelimiter = messageWithDelimiter.getBytes(StandardCharsets.UTF_8);

        for (SelectionKey key : selector.keys()) {
            if (key.isValid() && key.channel() instanceof SocketChannel) {
                SocketChannel channel = (SocketChannel) key.channel();
                if (channel != origin) {
                    ByteBuffer buffer = ByteBuffer.wrap(dataWithDelimiter);
                    while (buffer.hasRemaining()) {
                        channel.write(buffer);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            new Server().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
