// 차트 랜더링 컴포넌트
// src/components/CandleChart.tsx
import React, { useEffect, useRef } from 'react';
import { createChart, IChartApi, ISeriesApi, BarData } from 'lightweight-charts';

interface CandleChartProps {
  data: BarData[];
}

const CandleChart: React.FC<CandleChartProps> = ({ data }) => {
  const chartRef = useRef<IChartApi | null>(null);
  const containerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (!containerRef.current) return;

    chartRef.current = createChart(containerRef.current, {
      width: containerRef.current.clientWidth,
      height: 400,
      layout: { background: { color: '#fff' }, textColor: '#000' },
      grid: { vertLines: { color: '#eee' }, horzLines: { color: '#eee' } },
      timeScale: { timeVisible: true },
    });

    const series: ISeriesApi<'Candlestick'> = chartRef.current.addCandlestickSeries({
      upColor: '#26a69a',
      downColor: '#ef5350',
      wickUpColor: '#26a69a',
      wickDownColor: '#ef5350',
    });

    series.setData(data);
    chartRef.current.timeScale().fitContent();

    const handleResize = () => {
      if (containerRef.current) {
        chartRef.current?.applyOptions({ width: containerRef.current.clientWidth });
      }
    };

    window.addEventListener('resize', handleResize);
    return () => {
      window.removeEventListener('resize', handleResize);
      chartRef.current?.remove();
    };
  }, [data]);

  return <div ref={containerRef} />;
};

export default CandleChart;
