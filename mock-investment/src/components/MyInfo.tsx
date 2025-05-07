import React from 'react';
import Header from './Header';

interface CryptoAsset {
  name: string;
  symbol: string;
  amount: number;
  value: number;
  change: number;
  percentage: number;
}

interface Transaction {
  date: string;
  currency: string;
  type: 'buy' | 'sell';
  price: number;
  amount: number;
  total: number;
  status: string;
}

export interface UserInfoProps {
  name: string;
  email: string;
  phone: string;
  joinDate: string;
  totalBalance: number;
  goalAmount: number;
  currentAmount: number;
  goalPercentage: number;
  assets: CryptoAsset[];
  transactions: Transaction[];
}

const MyInfo: React.FC<UserInfoProps> = ({
  name,
  email,
  phone,
  joinDate,
  totalBalance,
  goalAmount,
  currentAmount,
  goalPercentage,
  assets,
  transactions
}) => {
  return (
    <div className="bg-white min-h-screen">
      <Header />

      {/* Main Content */}
      <div className="max-w-4xl mx-auto px-4 pt-6">
        <h2 className="text-xl text-blue-400 font-semibold mb-6">내 정보</h2>

        {/* Personal Info */}
        <div className="mb-8">
          <h3 className="text-base font-semibold mb-4 text-gray-700">개인 정보</h3>
          <div className="grid grid-cols-2 gap-8">
            <div>
              <div className="mb-4">
                <p className="text-gray-500 text-sm">이름</p>
                <p className="font-semibold">{name}</p>
              </div>
              <div>
                <p className="text-gray-500 text-sm">휴대폰 번호</p>
                <p>{phone}</p>
              </div>
            </div>
            <div>
              <div className="mb-4">
                <p className="text-gray-500 text-sm">이메일</p>
                <p>{email}</p>
              </div>
              <div>
                <p className="text-gray-500 text-sm">가입일</p>
                <p>{joinDate}</p>
              </div>
            </div>
          </div>
        </div>

        {/* Balance Info */}
        <div className="mb-8">
          <h3 className="text-base font-semibold mb-4 text-gray-700">보유자산 현황</h3>
          <div className="bg-blue-200 p-4 rounded-md flex justify-between items-center mb-4">
            <span className="font-semibold">총 보유자산</span>
            <span className="font-bold text-lg">₩{totalBalance.toLocaleString()}</span>
          </div>

          <div className="grid grid-cols-2 gap-6">
            <div>
              <div className="flex justify-between mb-2">
                <span className="text-sm">목표 금액</span>
                <span className="text-sm">₩{goalAmount.toLocaleString()}</span>
              </div>
              <div className="bg-gray-200 h-2 rounded-full overflow-hidden">
                <div 
                  className="bg-blue-400 h-full" 
                  style={{ width: `${goalPercentage}%` }}
                ></div>
              </div>
              <div className="text-right text-xs text-gray-500 mt-1">
                목표달성 {goalPercentage}%
              </div>
            </div>
            <div>
              <div className="flex justify-between mb-2">
                <span className="text-sm">목표 금액</span>
                <span className="text-sm">₩{currentAmount.toLocaleString()}</span>
              </div>
              <div className="bg-gray-200 h-2 rounded-full overflow-hidden">
                <div 
                  className="bg-blue-400 h-full" 
                  style={{ width: `${100-goalPercentage}%` }}
                ></div>
              </div>
              <div className="text-right text-xs text-gray-500 mt-1">
                목표달성 {100-goalPercentage}%
              </div>
            </div>
          </div>
        </div>

        {/* Asset Details */}
        <div className="mb-8">
          <h3 className="text-base font-semibold mb-4 text-gray-700">보유 코인 상세</h3>
          <div className="border rounded-md overflow-hidden">
            <table className="w-full">
              <thead className="bg-gray-50">
                <tr className="text-left text-gray-500 text-sm">
                  <th className="py-3 px-4">코인</th>
                  <th className="py-3 px-4">보유수량</th>
                  <th className="py-3 px-4">시장가치</th>
                  <th className="py-3 px-4">변동</th>
                  <th className="py-3 px-4">비중</th>
                </tr>
              </thead>
              <tbody>
                {assets.map((asset, index) => (
                  <tr key={index} className="border-t">
                    <td className="py-4 px-4">
                      <div className="flex items-center">
                        <div className={`w-6 h-6 rounded-full flex items-center justify-center text-white mr-2 ${
                          asset.symbol === 'BTC' ? 'bg-yellow-500' : 
                          asset.symbol === 'ETH' ? 'bg-blue-500' : 'bg-green-500'
                        }`}>
                          {asset.symbol.charAt(0)}
                        </div>
                        <div>
                          <p className="font-semibold">{asset.name}</p>
                          <p className="text-xs text-gray-500">{asset.symbol}</p>
                        </div>
                      </div>
                    </td>
                    <td className="py-4 px-4">{asset.amount} {asset.symbol}</td>
                    <td className="py-4 px-4">₩{asset.value.toLocaleString()}</td>
                    <td className={`py-4 px-4 ${asset.change >= 0 ? 'text-green-500' : 'text-red-500'}`}>
                      {asset.change > 0 ? '+' : ''}{asset.change}%
                    </td>
                    <td className="py-4 px-4">
                      <div className="flex items-center">
                        <span className="mr-2">{asset.percentage}%</span>
                        <div className="w-16 bg-gray-200 h-1 rounded-full overflow-hidden">
                          <div 
                            className={`h-full ${
                              asset.symbol === 'BTC' ? 'bg-yellow-500' : 
                              asset.symbol === 'ETH' ? 'bg-blue-500' : 'bg-green-500'
                            }`}
                            style={{ width: `${asset.percentage}%` }}
                          ></div>
                        </div>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        {/* Transaction History */}
        <div className="mb-8">
          <h3 className="text-base font-semibold mb-4 text-gray-700">주문 관리</h3>
          <div className="border rounded-md overflow-hidden">
            <table className="w-full">
              <thead className="bg-gray-50">
                <tr className="text-left text-gray-500 text-sm">
                  <th className="py-3 px-4">날짜/시간</th>
                  <th className="py-3 px-4">코인</th>
                  <th className="py-3 px-4">유형</th>
                  <th className="py-3 px-4">가격</th>
                  <th className="py-3 px-4">수량</th>
                  <th className="py-3 px-4">총액</th>
                  <th className="py-3 px-4">상태</th>
                </tr>
              </thead>
              <tbody>
                {transactions.map((tx, index) => (
                  <tr key={index} className="border-t">
                    <td className="py-3 px-4 text-sm">{tx.date}</td>
                    <td className="py-3 px-4">{tx.currency}</td>
                    <td className="py-3 px-4">
                      <span className={`text-xs px-2 py-1 rounded ${
                        tx.type === 'buy' ? 'bg-green-100 text-green-600' : 'bg-red-100 text-red-600'
                      }`}>
                        {tx.type === 'buy' ? '매수' : '매도'}
                      </span>
                    </td>
                    <td className="py-3 px-4">₩{tx.price.toLocaleString()}</td>
                    <td className="py-3 px-4">{tx.amount} {tx.currency}</td>
                    <td className="py-3 px-4">₩{tx.total.toLocaleString()}</td>
                    <td className="py-3 px-4">
                      <span className="text-blue-500">{tx.status}</span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MyInfo;