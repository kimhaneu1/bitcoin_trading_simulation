import { useEffect, useRef, useState } from 'react';
import { createChart, UTCTimestamp, Time } from 'lightweight-charts';
import axios from 'axios';
import { CandleData } from '@/lib/types';

interface CandleChartProps {
  symbol: string;
  timeframe?: string;
}

// lightweight-charts 호환 캔들 데이터 타입
interface ChartCandle {
  time: Time;
  open: number;
  high: number;
  low: number;
  close: number;
}

const CandleChart = ({ symbol, timeframe = 'days' }: CandleChartProps) => {
  const chartRef = useRef<HTMLDivElement>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    // 차트와 캔들데이터를 가져오는 함수
    const initChart = async () => {
      if (!chartRef.current) {
        console.log('차트 컨테이너 ref가 없습니다.');
        return;
      }
      
      try {
        setIsLoading(true);
        setError(null);
        
        console.log('차트 생성 시작...');
        console.log('차트 컨테이너 크기:', chartRef.current.clientWidth, 'x', chartRef.current.clientHeight);
        
        // 컨테이너 요소에 차트 생성
        const chartElement = chartRef.current;
        const chart = createChart(chartElement, {
          width: chartElement.clientWidth,
          height: 400,
          layout: {
            background: { color: '#1E1E1E' },
            textColor: '#D9D9D9',
          },
          grid: {
            vertLines: { color: '#2B2B43' },
            horzLines: { color: '#2B2B43' },
          },
        });
        
        console.log('차트 객체 생성 완료:', chart);
        
        // 캔들 시리즈 추가
        const candleSeries = chart.addCandlestickSeries({
          upColor: '#26a69a',
          downColor: '#ef5350',
          borderVisible: false,
          wickUpColor: '#26a69a',
          wickDownColor: '#ef5350',
        });
        
        console.log('캔들 시리즈 생성 완료');
        
        // API 요청 파라미터
        const params = {
          market: symbol,
          unit: timeframe,
          count: 90,
        };
        console.log('API 요청 파라미터:', params);
        
        // API에서 캔들 데이터 가져오기
        console.log('API 요청 시작...');
        try {
          const response = await axios.get(`/api/upbit/candles`, { params });
          console.log('API 응답 성공:', response);
          console.log('API 데이터:', response.data);
          
          // 응답이 배열이 아니거나 비어있는 경우 처리
          if (!Array.isArray(response.data) || response.data.length === 0) {
            console.error('API 응답 데이터가 유효하지 않습니다:', response.data);
            setError('유효하지 않은 데이터 형식입니다');
            setIsLoading(false);
            return;
          }
          
          // 데이터 구조 확인을 위해 첫 번째 항목 로깅
          console.log('첫 번째 캔들 데이터:', response.data[0]);
          
          // 데이터 포맷 변환 - 타입 이슈 해결
          const candleData = response.data.map((item: any) => {
            // 날짜 문자열을 timestamp로 변환 (초 단위)
            const timestamp = Math.floor(new Date(item.candle_date_time_utc).getTime() / 1000);
            
            return {
              // UTCTimestamp 타입으로 변환
              time: timestamp as UTCTimestamp,
              open: item.opening_price,
              high: item.high_price,
              low: item.low_price,
              close: item.trade_price,
            };
          });
          
          console.log('변환된 차트 데이터:', candleData);
          
          // 차트에 데이터 설정
          candleSeries.setData(candleData);
          console.log('차트 데이터 설정 완료');
          
        } catch (apiError: any) {
          console.error('API 요청 실패:', apiError);
          console.error('에러 상세:', apiError.response?.data || apiError.message);
          setError(`API 요청 실패: ${apiError.message}`);
          setIsLoading(false);
          return;
        }
        
        // 차트 크기 조정
        const handleResize = () => {
          if (chartElement) {
            console.log('창 크기 조정 감지, 차트 리사이징...');
            chart.applyOptions({ width: chartElement.clientWidth });
          }
        };
        
        window.addEventListener('resize', handleResize);
        console.log('리사이즈 이벤트 리스너 추가됨');
        
        // 정리 함수
        return () => {
          console.log('차트 컴포넌트 언마운트, 정리 실행...');
          window.removeEventListener('resize', handleResize);
          chart.remove();
        };
      } catch (err: any) {
        console.error('차트 생성 오류:', err);
        console.error('오류 스택:', err.stack);
        setError(`차트 생성 실패: ${err.message}`);
      } finally {
        setIsLoading(false);
      }
    };
    
    console.log('차트 초기화 시작, 심볼:', symbol, '타임프레임:', timeframe);
    initChart();
  }, [symbol, timeframe]);
  
  if (isLoading) {
    return <div className="h-64 flex items-center justify-center">로딩 중...</div>;
  }
  
  if (error) {
    return <div className="h-64 flex items-center justify-center text-red-500">{error}</div>;
  }
  
  return (
    <div className="w-full">
      <div 
        ref={chartRef} 
        className="w-full h-96 rounded border border-gray-700"
        style={{ minHeight: '400px' }}
      />
    </div>
  );
};

export default CandleChart;