import axios from 'axios';

// Upbit API 요청을 위한 클라이언트
const upbitClient = axios.create({
  baseURL: 'https://api.upbit.com/v1',
  timeout: 10000,
  headers: {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  }
});

// 내부 API 요청을 위한 클라이언트
const apiClient = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  }
});

export const api = {
  // 마켓 코드 조회
  async getTicker(market: string) {
    return apiClient.get(`/upbit/ticker?market=${market}`);
  },
  
  // 캔들 데이터 조회
  async getCandles(market: string, unit: string, count: number, to?: string) {
    return apiClient.get(`/upbit/candles`, {
      params: { market, unit, count, to }
    });
  },
  
  // 로그인
  async login(email: string, password: string) {
    return apiClient.post('/auth', { email, password, type: 'login' });
  },
  
  // 회원가입
  async register(email: string, password: string, name: string) {
    return apiClient.post('/auth', { email, password, name, type: 'register' });
  }
};