import React, { useState, useCallback, useRef } from 'react';
import { useUpbitSocket } from '../hooks/useUpbitSocket';
import { usePreserveWindowScroll } from '../hooks/usePreserveWindowScroll';

interface DepthItem {
  price: number;
  volume: number;
}

const MiniDepthChart: React.FC = () => {
  const [sellOrders, setSellOrders] = useState<DepthItem[]>([]);
  const [buyOrders, setBuyOrders] = useState<DepthItem[]>([]);
  
  usePreserveWindowScroll([sellOrders, buyOrders]);

  // Using refs to compare the current data without triggering re-renders
  const prevAskRef = useRef<{price: number, volume: number} | null>(null);
  const prevBidRef = useRef<{price: number, volume: number} | null>(null);

  const handleTrade = useCallback((data: any) => {
    if (data.type !== 'trade') return;

    const newAsk = { price: data.best_ask_price, volume: data.best_ask_size };
    const newBid = { price: data.best_bid_price, volume: data.best_bid_size };

    // Compare with refs instead of state to avoid dependency issues
    if (
      !prevAskRef.current || 
      prevAskRef.current.price !== newAsk.price || 
      prevAskRef.current.volume !== newAsk.volume
    ) {
      prevAskRef.current = newAsk;
      setSellOrders([newAsk]);
    }

    if (
      !prevBidRef.current ||
      prevBidRef.current.price !== newBid.price || 
      prevBidRef.current.volume !== newBid.volume
    ) {
      prevBidRef.current = newBid;
      setBuyOrders([newBid]);
    }
  }, []); // Empty dependency array for better performance

  useUpbitSocket(handleTrade);

  // Calculate max volume for proper scaling
  const maxVolume = Math.max(
    ...sellOrders.map(order => order.volume),
    ...buyOrders.map(order => order.volume),
    0.1 // Fallback to prevent division by zero
  );

  return (
    <div className="bg-white rounded-md shadow border p-4">
      <h3 className="text-center text-sm font-semibold text-gray-700 mb-2">미니 차트</h3>

      {/* Optional debugging information */}
      <div className="text-xs text-gray-400 mb-2">
        <p>매도: {sellOrders.length > 0 ? `${sellOrders[0].price.toLocaleString()} ₩ (${sellOrders[0].volume.toFixed(4)})` : '로딩 중...'}</p>
        <p>매수: {buyOrders.length > 0 ? `${buyOrders[0].price.toLocaleString()} ₩ (${buyOrders[0].volume.toFixed(4)})` : '로딩 중...'}</p>
      </div>

      {/* Sell orders (red) */}
      <div className="space-y-1 mb-4">
        {sellOrders.map((item, i) => (
          <div key={`sell-${item.price}-${i}`} className="flex items-center text-red-500 text-xs">
            <div 
              className="bg-red-200 h-4 mr-2" 
              style={{ width: `${(item.volume / maxVolume) * 100}%`, transition: 'width 0.3s ease-in-out' }} 
            />
            <span>{item.price.toLocaleString()} ₩</span>
          </div>
        ))}
      </div>

      {/* Buy orders (blue) */}
      <div className="space-y-1">
        {buyOrders.map((item, i) => (
          <div key={`buy-${item.price}-${i}`} className="flex items-center text-blue-500 text-xs">
            <div 
              className="bg-blue-200 h-4 mr-2" 
              style={{ width: `${(item.volume / maxVolume) * 100}%`, transition: 'width 0.3s ease-in-out' }} 
            />
            <span>{item.price.toLocaleString()} ₩</span>
          </div>
        ))}
      </div>
    </div>
  );
};

export default MiniDepthChart;