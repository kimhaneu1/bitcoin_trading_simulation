// src/components/TradeHistory.tsx

import React, { useState, useCallback } from 'react';
import { useUpbitSocket } from '../hooks/useUpbitSocket';
import { usePreserveWindowScroll } from '../hooks/usePreserveWindowScroll';

interface TradeItem {
  time: string;
  price: string;
  volume: string;
  amount: string;
  side: 'buy' | 'sell';
}

// 시간 포맷 변환 함수
const formatTime = (hhmmss: string) => {
  return `${hhmmss.slice(0, 2)}:${hhmmss.slice(2, 4)}:${hhmmss.slice(4, 6)}`;
};

export default function TradeHistory() {
  const [trades, setTrades] = useState<TradeItem[]>([]);

  usePreserveWindowScroll([trades]);

  const handleTrade = useCallback((data: any) => {
    if (data.type !== 'trade') return;

    const trade: TradeItem = {
      time: formatTime(data.trade_time),
      price: data.trade_price.toLocaleString(),
      volume: data.trade_volume.toFixed(8),
      amount: Math.round(data.trade_price * data.trade_volume).toLocaleString(),
      side: data.ask_bid, // 'buy' or 'sell'
    };

    setTrades((prev) => [trade, ...prev.slice(0, 99)]);
  }, []);

  useUpbitSocket(handleTrade);

  return (
    <div className="bg-white rounded-xl shadow p-4">
      <h2 className="text-lg font-semibold border-b pb-2 mb-3">체결 내역</h2>
      <div className="overflow-auto max-h-80">
        <table className="w-full text-sm text-right">
          <thead>
            <tr className="text-gray-500 border-b">
              <th className="text-left">시간</th>
              <th>체결가</th>
              <th>체결량</th>
              <th>체결금액</th>
            </tr>
          </thead>
          <tbody>
            {trades.map((row, idx) => (
              <tr key={idx} className="border-b">
                <td className="text-left text-gray-600">{row.time}</td>
                <td className="text-blue-600">{row.price}₩</td>
                <td className={row.side === 'buy' ? 'text-blue-500' : 'text-red-500'}>
                  {row.volume}
                </td>
                <td>{row.amount}₩</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
