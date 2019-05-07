package util;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import bean.Message;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class WebSocketConnection extends WebSocketListener {

    private WebSocket webSocket;

    private HashMap<Long, MessageListener> messageListenerHashMap = new HashMap<>();

    private MessageListener defaultMessageListener;

    private NameChangeListener nameChangeListener;

    public void setIconChangeListener(IconChangeListener iconChangeListener) {
        this.iconChangeListener = iconChangeListener;
    }

    private IconChangeListener iconChangeListener;

    public void setNameChangeListener(NameChangeListener nameChangeListener) {
        this.nameChangeListener = nameChangeListener;
    }

    public void setDefaultMessageListener(MessageListener defaultMessageListener) {
        this.defaultMessageListener = defaultMessageListener;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public static WebSocketConnection wsConnect(String url) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.SECONDS).readTimeout(2, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url(url).build();
        WebSocketConnection webSocketConnection = new WebSocketConnection();
        webSocketConnection.webSocket = okHttpClient.newWebSocket(request, webSocketConnection);
        return webSocketConnection;
    }

    public void setMessageListener(long roomId, MessageListener listener) {
        messageListenerHashMap.put(roomId, listener);
    }

    public void removeMessageListener(long roomId) {
        messageListenerHashMap.remove(roomId);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        System.out.println(text);
        try {
            JSONObject input = new JSONObject(text);
            Message message = new Message();
            int messageType = input.getInt("messageType");
            message.setMessageType(messageType);
            message.setRommId(input.getLong("roomId"));
            message.setTime(input.getLong("time"));
            message.setUsername(input.getLong("username"));
            message.setName(input.getString("name"));
            message.setMsg(input.getString("message"));
            switch (messageType) {
                case 1:
                case 2:
                    // TODO:恢复名字
                    nameChangeListener.nameChange(message);
                    break;
                case 3:
                    //TODO:接受并显示消息
                    MessageListener messageListener = messageListenerHashMap.getOrDefault(message.getRommId(), null);
                    if (messageListener != null) {
                        messageListener.onMessage(message);
                    } else if (defaultMessageListener != null) {
                        defaultMessageListener.onMessage(message);
                    } else {
                        System.err.println("No listener handle message:" + message);
                    }
                    break;
                case 4:
                    //TODO:提醒大家有新公告发布了
                    iconChangeListener.iconChange(message);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public interface MessageListener {
        void onMessage(Message message);
    }

    public interface NameChangeListener {
        void nameChange(Message message);
    }

    public interface IconChangeListener {
        void iconChange(Message message);
    }
}
