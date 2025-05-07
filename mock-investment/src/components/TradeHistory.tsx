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

// UTC 시간을 KST(한국 시간)으로 변환하는 함수
const convertToKST = (timeStr: string) => {
  const [hourStr, minuteStr, secondStr] = timeStr.split(':');
  let hour = parseInt(hourStr, 10);
  const minute = parseInt(minuteStr, 10);
  const second = parseInt(secondStr, 10);

  // UTC → KST 변환 (UTC + 9시간)
  hour = (hour + 9) % 24;

  return `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}:${second.toString().padStart(2, '0')}`;
};

// 시간을 오전/오후 형식으로 포맷팅
const formatTime = (timeStr: string) => {
  const [hourStr, minuteStr, secondStr] = timeStr.split(':');
  const hour = parseInt(hourStr, 10);
  const minute = parseInt(minuteStr, 10);
  const second = parseInt(secondStr, 10);

  const period = hour < 12 ? '오전' : '오후';
  const hour12 = hour % 12 === 0 ? 12 : hour % 12;

  return `${period} ${hour12}:${minute.toString().padStart(2, '0')}:${second
    .toString()
    .padStart(2, '0')}`;
};

export default function TradeHistory() {
  const [trades, setTrades] = useState<TradeItem[]>([]);

  usePreserveWindowScroll([trades]);

  const handleTrade = useCallback((data: any) => {
    if (data.type !== 'trade') return;

    // 원본 UTC 시간
    let originalTime = data.trade_time;
    
    // 시간 형식이 HHMMSSsss 형식이면 HH:MM:SS 형식으로 변환
    if (!originalTime.includes(':')) {
      const hour = originalTime.slice(0, 2);
      const minute = originalTime.slice(2, 4);
      const second = originalTime.slice(4, 6);
      originalTime = `${hour}:${minute}:${second}`;
    }
    
    // UTC → KST 변환
    const kstTime = convertToKST(originalTime);
    
    // 오전/오후 포맷으로 변환
    const formattedTime = formatTime(kstTime);
    
    // 원본과 변환된 시간 로깅 (디버깅용)
    console.log(`원본 시간(UTC): ${originalTime} → 변환 시간(KST): ${kstTime} → 표시: ${formattedTime}`);

    const trade: TradeItem = {
      time: formattedTime, // KST로 변환된 시간
      price: data.trade_price.toLocaleString(),
      volume: data.trade_volume.toFixed(8),
      amount: Math.round(data.trade_price * data.trade_volume).toLocaleString(),
      side: data.ask_bid === 'ASK' ? 'sell' : 'buy',
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
              <th className="text-left">시간 (KST)</th>
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