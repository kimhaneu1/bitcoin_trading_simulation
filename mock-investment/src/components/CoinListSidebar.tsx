import React, { useEffect, useState, useCallback } from 'react';
import { useUpbitSocket } from '../hooks/useUpbitSocket';

interface CoinInfo {
  code: string;     // 예: KRW-BTC
  price: number;
  change: number;
}

const trackedCoins = ['KRW-BTC', 'KRW-ETH', 'KRW-XRP'];

export default function CoinListSidebar() {
  const [coinList, setCoinList] = useState<CoinInfo[]>([]);

  const handleTicker = useCallback((data: any) => {
    if (data.type !== 'ticker') return;

    setCoinList((prevList) => {
      const existing = prevList.find((item) => item.code === data.code);
      const changePercent = data.signed_change_rate * 100;

      const updatedCoin = {
        code: data.code,
        price: data.trade_price,
        change: changePercent,
      };

      if (existing) {
        return prevList.map((item) =>
          item.code === data.code ? updatedCoin : item
        );
      } else {
        return [...prevList, updatedCoin];
      }
    });
  }, []);

  useEffect(() => {
    const ws = new WebSocket('wss://api.upbit.com/websocket/v1');
    ws.binaryType = 'arraybuffer';

    ws.onopen = () => {
      const subscribeMsg = [
        { ticket: 'coin-list' },
        { type: 'ticker', codes: trackedCoins }
      ];
      ws.send(JSON.stringify(subscribeMsg));
    };

    ws.onmessage = (event) => {
      try {
        const text = new TextDecoder('utf-8').decode(event.data);
        const json = JSON.parse(text);
        handleTicker(json);
      } catch (e) {
        console.error('ticker 파싱 오류', e);
      }
    };

    return () => {
      ws.close();
    };
  }, [handleTicker]);

  return (
    <div className="bg-white rounded-md shadow border p-4">
      <h3 className="text-center font-semibold text-gray-700 mb-2">시세 목록</h3>
      <div className="divide-y text-sm">
        {coinList.map((coin, index) => {
          const symbol = coin.code.replace('KRW-', '');
          const changeClass =
            coin.change > 0
              ? 'text-green-600 text-xs'
              : coin.change < 0
              ? 'text-red-600 text-xs'
              : 'text-gray-500 text-xs';

          return (
            <div key={coin.code} className="py-2 flex justify-between items-center">
              <span className="font-medium">{symbol}</span>
              <div className="text-right">
                <p className="font-semibold">{coin.price.toLocaleString()} ₩</p>
                <p className={changeClass}>
                  {coin.change > 0 ? '+' : ''}
                  {coin.change.toFixed(2)}%
                </p>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
