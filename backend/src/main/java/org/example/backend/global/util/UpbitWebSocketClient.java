package org.example.backend.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Service
@RequiredArgsConstructor
public class UpbitWebSocketClient {

    private static final String WS_URL = "wss://api.upbit.com/websocket/v1";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, BigDecimal> priceCache = new ConcurrentHashMap<>();
    private final Set<String> subscribedSymbols = ConcurrentHashMap.newKeySet(); // 구독된 코인 목록
    private WebSocketClient webSocketClient;

    @PostConstruct
    public void init() {
        new Thread(this::connectWebSocket, "Upbit-WebSocket-Thread").start();
    }

    private void connectWebSocket() {
        try {
            URI uri = new URI(WS_URL);
            webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshake) {
                    log.info("✅ Upbit WebSocket 연결됨");
                }

                @Override
                public void onMessage(String message) {
                    handlePriceMessage(message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.warn("🔌 WebSocket 연결 종료: {}", reason);
                }

                @Override
                public void onError(Exception ex) {
                    log.error("❗ WebSocket 오류 발생", ex);
                }
            };

            webSocketClient.connectBlocking();
        } catch (Exception e) {
            log.error("❗ WebSocket 연결 실패", e);
        }
    }

    public void subscribe(String coinSymbol) {
        if (subscribedSymbols.contains(coinSymbol)) return;

        try {
            ArrayNode arrayNode = objectMapper.createArrayNode();

            ObjectNode ticketNode = objectMapper.createObjectNode();
            ticketNode.put("ticket", "price-subscription");
            arrayNode.add(ticketNode);

            ObjectNode typeNode = objectMapper.createObjectNode();
            typeNode.put("type", "trade");
            typeNode.putArray("codes").add("KRW-" + coinSymbol);
            typeNode.put("isOnlyRealtime", true);
            arrayNode.add(typeNode);

            String payload = objectMapper.writeValueAsString(arrayNode);
            webSocketClient.send(payload);
            subscribedSymbols.add(coinSymbol);

            log.info("📨 코인 구독 요청 전송됨: {}", coinSymbol);
        } catch (JsonProcessingException e) {
            log.error("❗ 구독 요청 직렬화 실패", e);
        }
    }

    private void handlePriceMessage(String message) {
        try {
            JsonNode json = objectMapper.readTree(message);
            String market = json.get("code").asText();  // 예: KRW-BTC
            BigDecimal price = json.get("trade_price").decimalValue();
            String coinSymbol = market.replace("KRW-", "");

            priceCache.put(coinSymbol, price);
        } catch (Exception e) {
            log.error("❗ 실시간 가격 메시지 처리 실패", e);
        }
    }

    public BigDecimal getPrice(String coinSymbol) {
        return priceCache.getOrDefault(coinSymbol, BigDecimal.ZERO);
    }
}
