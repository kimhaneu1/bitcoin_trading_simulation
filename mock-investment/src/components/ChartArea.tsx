import React, { useEffect, useState, useCallback } from 'react';
import CandleChart from './CandleChart';
import axios from 'axios';
import { CandlestickData, UTCTimestamp } from 'lightweight-charts';
import { useUpbitSocket } from '../hooks/useUpbitSocket';

// 1분봉 생성 유틸 함수
const createNewCandle = (timestamp: number, price: number): CandlestickData => {
  const time = Math.floor(timestamp / 1000 / 60) * 60 as UTCTimestamp; // ⏱ 분 단위 UTC timestamp
  return {
    time,
    open: price,
    high: price,
    low: price,
    close: price,
  };
};

const ChartArea: React.FC = () => {
  const [candleData, setCandleData] = useState<CandlestickData[]>([]);

  // 초기 1분봉 데이터 불러오기
  useEffect(() => {
    const fetchCandles = async () => {
      const res = await axios.get('https://api.upbit.com/v1/candles/minutes/1', {
        params: { market: 'KRW-BTC', count: 100 },
      });

      const formatted: CandlestickData[] = res.data.map((item: any) => ({
        time: Math.floor(new Date(item.candle_date_time_utc).getTime() / 1000) as UTCTimestamp,
        open: item.opening_price,
        high: item.high_price,
        low: item.low_price,
        close: item.trade_price,
      })).reverse();

      setCandleData(formatted);
    };

    fetchCandles();
  }, []);

  // 실시간 체결 데이터 처리
  const handleTrade = useCallback((data: any) => {
    if (!data?.trade_price || !data?.timestamp) return;

    const newCandleTime = Math.floor(data.timestamp / 1000 / 60) * 60 as UTCTimestamp;

    setCandleData((prev) => {
      if (prev.length === 0) {
        return [createNewCandle(data.timestamp, data.trade_price)];
      }

      const last = prev[prev.length - 1];

      if (last.time === newCandleTime) {
        // 기존 봉 업데이트
        const updated: CandlestickData = {
          ...last,
          close: data.trade_price,
          high: Math.max(last.high, data.trade_price),
          low: Math.min(last.low, data.trade_price),
        };
        return [...prev.slice(0, -1), updated];
      } else {
        // 새 봉 추가
        return [...prev, createNewCandle(data.timestamp, data.trade_price)];
      }
    });
  }, []);

  // WebSocket 구독
  useUpbitSocket(handleTrade);

  return (
    <div className="bg-white rounded-md p-4">
      <h2 className="text-xl font-bold text-center mb-4">KRW-BTC 실시간 1분봉 차트</h2>
      <CandleChart data={candleData} />
    </div>
  );
};

export default ChartArea;
