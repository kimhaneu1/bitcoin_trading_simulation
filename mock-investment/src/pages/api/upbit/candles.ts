import axios from 'axios';
import type { NextApiRequest, NextApiResponse } from 'next';

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
  console.log('캔들 API 요청 받음:', req.method);
  console.log('쿼리 파라미터:', req.query);

  // GET 요청만 허용
  if (req.method !== 'GET') {
    console.log('잘못된 HTTP 메서드:', req.method);
    return res.status(405).json({ error: 'Method not allowed' });
  }

  try {
    const { market, unit, count, to } = req.query;

    // 필수 파라미터 확인
    if (!market) {
      console.log('필수 파라미터 누락: market');
      return res.status(400).json({ error: 'Market parameter is required' });
    }

    // 업비트 API 엔드포인트 결정
    let endpoint = '';
    switch (String(unit)) {
      case 'minutes':
        endpoint = '/candles/minutes/1';
        break;
      case 'days':
        endpoint = '/candles/days';
        break;
      case 'weeks':
        endpoint = '/candles/weeks';
        break;
      case 'months':
        endpoint = '/candles/months';
        break;
      default:
        endpoint = '/candles/days';
    }
    
    console.log('선택된 엔드포인트:', endpoint);

    // 업비트 API 요청 파라미터
    const params: any = {
      market: market,
      count: count || 200
    };

    // 선택적 파라미터 추가
    if (to) {
      params.to = to;
    }
    
    console.log('업비트 API 요청 파라미터:', params);
    console.log('업비트 API URL:', `https://api.upbit.com/v1${endpoint}`);

    // 업비트 API 호출
    const response = await axios.get(`https://api.upbit.com/v1${endpoint}`, {
      params,
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    });
    
    console.log('업비트 API 응답 상태:', response.status);
    console.log('응답 데이터 개수:', Array.isArray(response.data) ? response.data.length : 'N/A');
    
    if (Array.isArray(response.data) && response.data.length > 0) {
      console.log('첫 번째 캔들 데이터 샘플:', response.data[0]);
    }

    // 성공 응답
    return res.status(200).json(response.data);
  } catch (error: any) {
    console.error('업비트 API 오류:', error.message);
    
    if (error.response) {
      console.error('응답 상태:', error.response.status);
      console.error('응답 데이터:', error.response.data);
    } else if (error.request) {
      console.error('요청은 보냈으나 응답이 없음:', error.request);
    }
    
    // 에러 응답
    return res.status(error.response?.status || 500).json({
      error: error.response?.data || 'Internal server error',
      message: error.message
    });
  }
}