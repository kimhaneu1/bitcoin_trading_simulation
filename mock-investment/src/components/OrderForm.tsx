import React, { useState } from 'react';

const OrderForm: React.FC = () => {
  const [isBuy, setIsBuy] = useState(true);
  const [price, setPrice] = useState('');  // 문자열 상태로 유지
  const [amount, setAmount] = useState(''); // 문자열 상태로 유지

  // 주문 총액 계산
  const total = (() => {
    const p = parseFloat(price);
    const a = parseFloat(amount);
    return !isNaN(p) && !isNaN(a) ? p * a : 0;
  })();

  // 가격 +/- 버튼
  const handlePriceChange = (diff: number) => {
    const current = parseInt(price.replace(/[^0-9]/g, '')) || 0;
    const updated = Math.max(current + diff, 0);
    setPrice(updated.toString());
  };

  // 주문 전 콘솔 출력
  const handleSubmit = () => {
    const p = parseFloat(price);
    const a = parseFloat(amount);
    if (isNaN(p) || isNaN(a) || p <= 0 || a <= 0) {
      alert('유효한 가격과 수량을 입력해주세요.');
      return;
    }

    const orderType = isBuy ? '매수' : '매도';
    console.log(`[${orderType} 주문]`);
    console.log(`가격: ${p}₩`);
    console.log(`수량: ${a}`);
    console.log(`총액: ${total.toLocaleString()}₩`);
  };

  return (
    <div className="bg-white rounded-md shadow border p-4 text-sm">
      {/* 탭 전환 */}
      <div className="flex mb-4 border-b">
        <button
          className={`flex-1 py-2 font-semibold ${isBuy ? 'text-blue-600 border-b-2 border-blue-600' : 'text-gray-500'}`}
          onClick={() => setIsBuy(true)}
        >
          매수
        </button>
        <button
          className={`flex-1 py-2 font-semibold ${!isBuy ? 'text-red-600 border-b-2 border-red-600' : 'text-gray-500'}`}
          onClick={() => setIsBuy(false)}
        >
          매도
        </button>
      </div>

      {/* 매수가격 */}
      <label className="block mb-1 text-gray-600">매수가격 (KRW)</label>
      <div className="flex items-center mb-3">
        <button onClick={() => handlePriceChange(-1000)} className="px-2 py-1 border rounded-l">-</button>
        <input
          type="text"
          value={price}
          onChange={(e) => setPrice(e.target.value.replace(/[^0-9]/g, ''))}
          className="w-full px-2 py-1 border-t border-b text-right"
          placeholder="가격 입력"
        />
        <button onClick={() => handlePriceChange(1000)} className="px-2 py-1 border rounded-r">+</button>
      </div>

      {/* 수량 입력 */}
      <label className="block mb-1 text-gray-600">주문수량 (BTC)</label>
      <input
        type="text"
        value={amount}
        onChange={(e) => setAmount(e.target.value.replace(/[^0-9.]/g, ''))}
        className="w-full px-2 py-1 border rounded mb-3 text-right"
        placeholder="수량 입력"
      />

      {/* 총액 출력 */}
      <label className="block mb-1 text-gray-600">주문총액 (KRW)</label>
      <input
        type="text"
        value={total.toLocaleString()}
        readOnly
        className="w-full px-2 py-1 border rounded mb-4 bg-gray-50 text-right text-gray-600"
      />

      {/* 주문 버튼 */}
      <button
        onClick={handleSubmit}
        className={`w-full py-2 rounded font-semibold ${
          isBuy ? 'bg-blue-600 text-white' : 'bg-red-600 text-white'
        }`}
      >
        {isBuy ? '매수 주문' : '매도 주문'}
      </button>
    </div>
  );
};

export default OrderForm;
