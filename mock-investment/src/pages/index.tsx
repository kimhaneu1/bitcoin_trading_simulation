import React from 'react';
import Header from '../components/Header';
import ChartArea from '../components/ChartArea';
import OrderForm from '../components/OrderForm';
import OrderBook from '../components/OrderBook';
import MiniDepthChart from '../components/MiniDepthChart';
import TradeHistory from '../components/TradeHistory';
import CoinListSidebar from '../components/CoinListSidebar';
import AdvertisementBanner from '../components/AdvertisementBanner';

export default function Home() {
  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      <Header />

      {/* 본문 영역 */}
      <div className="flex flex-col lg:flex-row flex-1">
        {/* 좌측: 메인 (lg 기준 3/4) */}
        <div className="w-full lg:w-3/4 p-4 space-y-4">
          <ChartArea />

          {/* 하단 3단 → 모바일에서 1단 */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            <OrderBook />
            <OrderForm />
            <MiniDepthChart />
          </div>
          {/* 체결 내역 추가 */}
          <TradeHistory />
        </div>

        {/* 우측: 사이드바 (lg 이상에서만 오른쪽에 보임) */}
        <div className="w-full lg:w-1/4 p-4 space-y-4 border-t lg:border-t-0 lg:border-l">
          <CoinListSidebar />
          <AdvertisementBanner />
        </div>
      </div>
    </div>
  );
}
