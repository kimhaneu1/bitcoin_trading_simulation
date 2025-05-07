import React, { useState, useCallback, useRef } from 'react';
import { useUpbitSocket } from '../hooks/useUpbitSocket';
import { usePreserveWindowScroll } from '../hooks/usePreserveWindowScroll';

const OrderBook: React.FC = () => {
  const [sell, setSell] = useState({ price: 0, quantity: 0 });
  const [buy, setBuy] = useState({ price: 0, quantity: 0 });
  const [currentPrice, setCurrentPrice] = useState(0);
  
  usePreserveWindowScroll([sell, buy, currentPrice]);
    
  // Using refs to track previous values without causing re-renders
  const prevDataRef = useRef({
    sellPrice: 0,
    sellQuantity: 0,
    buyPrice: 0,
    buyQuantity: 0,
    tradePrice: 0
  });

  const handleTrade = useCallback((data: any) => {
    if (data.type !== 'trade') return;

    const prevData = prevDataRef.current;
    let needsUpdate = false;
    
    // Check sell order changes
    if (
      prevData.sellPrice !== data.best_ask_price ||
      prevData.sellQuantity !== data.best_ask_size
    ) {
      prevData.sellPrice = data.best_ask_price;
      prevData.sellQuantity = data.best_ask_size;
      setSell({ price: data.best_ask_price, quantity: data.best_ask_size });
      needsUpdate = true;
    }

    // Check buy order changes
    if (
      prevData.buyPrice !== data.best_bid_price ||
      prevData.buyQuantity !== data.best_bid_size
    ) {
      prevData.buyPrice = data.best_bid_price;
      prevData.buyQuantity = data.best_bid_size;
      setBuy({ price: data.best_bid_price, quantity: data.best_bid_size });
      needsUpdate = true;
    }

    // Check current price changes
    if (prevData.tradePrice !== data.trade_price) {
      prevData.tradePrice = data.trade_price;
      setCurrentPrice(data.trade_price);
      needsUpdate = true;
    }

    // Debug performance if needed
    if (needsUpdate) {
      // console.log('OrderBook updated:', new Date().toISOString());
    }
  }, []); // Empty dependency array

  useUpbitSocket(handleTrade);

  // Determine price trend (up, down, or neutral)
  const priceClass = useRef('text-blue-600').current;
  
  // Add transition effects for smoother updates
  return (
    <div className="bg-white rounded-md shadow border p-4 text-sm">
      <h3 className="text-center font-semibold text-gray-700 mb-2">호가창</h3>

      {/* Sell order (red) */}
      <div className="mb-2 text-red-500">
        <div className="flex justify-between">
          <span>{sell.quantity > 0 ? sell.quantity.toFixed(4) : '0.0000'}</span>
          <span>{sell.price > 0 ? sell.price.toLocaleString() : '0'} ₩</span>
        </div>
        <div className="w-full bg-gray-100 h-1 my-1">
          <div 
            className="bg-red-400 h-1" 
            style={{ 
              width: `${Math.min(sell.quantity * 100, 100)}%`, 
              transition: 'width 0.3s ease-out' 
            }} 
          />
        </div>
      </div>

      {/* Current trade price with animation */}
      <div className={`flex justify-between font-bold py-1 border-t border-b ${priceClass}`}>
        <span>체결</span>
        <span className="transition-all duration-300">
          {currentPrice > 0 ? currentPrice.toLocaleString() : '0'} ₩
        </span>
      </div>

      {/* Buy order (green) */}
      <div className="mt-2 text-green-600">
        <div className="w-full bg-gray-100 h-1 my-1">
          <div 
            className="bg-green-400 h-1" 
            style={{ 
              width: `${Math.min(buy.quantity * 100, 100)}%`, 
              transition: 'width 0.3s ease-out' 
            }} 
          />
        </div>
        <div className="flex justify-between">
          <span>{buy.quantity > 0 ? buy.quantity.toFixed(4) : '0.0000'}</span>
          <span>{buy.price > 0 ? buy.price.toLocaleString() : '0'} ₩</span>
        </div>
      </div>
    </div>
  );
};

export default OrderBook;