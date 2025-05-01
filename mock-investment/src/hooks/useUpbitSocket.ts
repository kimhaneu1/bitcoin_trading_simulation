// 웹소켓 연결을 관리하는 커스텀 훅

// src/hooks/useUpbitSocket.ts
import { useEffect, useRef } from 'react';

type TradeCallback = (data: any) => void;

export function useUpbitSocket(onTrade: TradeCallback) {
  const socketRef = useRef<WebSocket | null>(null);

  useEffect(() => {
    if (socketRef.current) return;

    const ws = new WebSocket('wss://api.upbit.com/websocket/v1');
    socketRef.current = ws;

    ws.binaryType = 'arraybuffer';

    ws.onopen = () => {
      console.log('[✅ WebSocket 연결됨]');
      const msg = [
        { ticket: 'test' },
        { type: 'trade', codes: ['KRW-BTC'] }
      ];
      ws.send(JSON.stringify(msg));
      console.log('[📨 구독 메시지 전송 완료]');
    };

    ws.onmessage = (event) => {
      try {
        const text = new TextDecoder('utf-8').decode(event.data);
        const json = JSON.parse(text);
        console.log('[📡 수신 데이터]', json);
        onTrade(json);
      } catch (err) {
        console.error('📛 데이터 파싱 실패:', err);
      }
    };

    ws.onerror = (e) => console.error('❗ WebSocket 에러:', e);
    ws.onclose = () => console.warn('❌ WebSocket 종료됨');

    return () => {
      ws.close();
      socketRef.current = null;
      console.log('[🔌 WebSocket 수동 종료]');
    };
  }, [onTrade]);
}
