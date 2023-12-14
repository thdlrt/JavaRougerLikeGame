package com.game.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class NetWork {
    private SocketChannel socketChannel;
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

    public int connect(String hostname, int port) throws Exception {

        socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(hostname, port));
        socketChannel.configureBlocking(false);
        // 等待连接完成
        while (!socketChannel.finishConnect()) {

        }
        int clientId = receiveClientId(socketChannel);
        System.out.println("Connected to the server. clientId=" + clientId);
        return clientId;
    }
    public int receiveClientId(SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        while (buffer.hasRemaining()) {
            if (channel.read(buffer) == -1) {
                throw new IOException("Connection closed, could not read client ID");
            }
        }
        buffer.flip();
        return buffer.getInt();
    }

    public void disconnect() throws Exception {
        socketChannel.close();
        System.out.println("Disconnected from the server.");
    }

    public void send(String message) throws Exception {

        writeBuffer.clear();
        writeBuffer.put(message.getBytes(StandardCharsets.UTF_8));
        writeBuffer.flip();
        while (writeBuffer.hasRemaining()) {
            socketChannel.write(writeBuffer);
        }
    }

    private StringBuilder incompleteMessage = new StringBuilder();

    public String receive() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = socketChannel.read(buffer);
        if (bytesRead <= 0) {
            return null;
        }

        buffer.flip();
        incompleteMessage.append(StandardCharsets.UTF_8.decode(buffer).toString());

        // 检查是否包含完整消息（检查分隔符）
        int delimiterIndex = incompleteMessage.indexOf("\n");
        if (delimiterIndex != -1) {
            String completeMessage = incompleteMessage.substring(0, delimiterIndex);
            incompleteMessage.delete(0, delimiterIndex + 1); // 移除已处理的消息部分
            return completeMessage;
        }

        return null;
    }

    public static void main(String[] args) {
        NetWork client = new NetWork();
        try {
            client.connect("localhost", 12345); // 连接服务器
            client.send("Hello, Server!"); // 发送信息
            // 接收信息
            String response = client.receive();
            if (response != null) {
                System.out.println("Server response: " + response);
            }
            client.disconnect(); // 断开连接
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
