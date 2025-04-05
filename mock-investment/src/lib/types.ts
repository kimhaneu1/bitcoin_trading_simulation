import { Time, UTCTimestamp } from 'lightweight-charts';

// 업비트 시세 데이터 타입
export interface TickerData {
  market: string;                 // 거래 마켓 (예: KRW-BTC)
  trade_price: number;            // 현재가
  signed_change_rate: number;     // 전일 대비 변화율
  acc_trade_price_24h?: number;   // 24시간 누적 거래금액
  timestamp?: number;             // 타임스탬프
}

// 업비트 캔들 데이터 타입
export interface CandleData {
  market: string;                 // 마켓명
  candle_date_time_utc: string;   // 캔들 기준 시각(UTC)
  opening_price: number;          // 시가
  high_price: number;             // 고가
  low_price: number;              // 저가
  trade_price: number;            // 종가
  timestamp: number;              // 해당 캔들에서 마지막 틱이 저장된 시각
  candle_acc_trade_volume?: number;// 누적 거래량
}

// lightweight-charts 라이브러리용 캔들 데이터 타입
export interface ChartCandle {
  time: Time;  // Time 타입 사용 (lightweight-charts의 타입)
  open: number;  // Opening price
  high: number;  // High price
  low: number;   // Low price
  close: number; // Closing price
}

// 사용자 타입
export interface User {
  id: string;
  email: string;
  name: string;
}

// 인증 응답 타입
export interface AuthResponse {
  user: User;
  token: string;
}