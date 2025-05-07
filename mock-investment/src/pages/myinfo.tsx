import React from 'react';
import MyInfo, { UserInfoProps } from '../components/MyInfo';

const MyInfoPage: React.FC = () => {
  // 샘플 데이터
  const userData: UserInfoProps = {
    name: "홍길동",
    email: "hong@example.com",
    phone: "010-1234-5678",
    joinDate: "2023년 10월 15일",
    totalBalance: 12450000,
    goalAmount: 5000000,
    currentAmount: 7450000,
    goalPercentage: 40,
    assets: [
      {
        name: "비트코인",
        symbol: "BTC",
        amount: 0.12,
        value: 4800000,
        change: 2.3,
        percentage: 64.4
      },
      {
        name: "이더리움",
        symbol: "ETH",
        amount: 0.85,
        value: 1950000,
        change: -1.5,
        percentage: 26.2
      },
      {
        name: "리플",
        symbol: "XRP",
        amount: 1200,
        value: 700000,
        change: 5.7,
        percentage: 9.4
      }
    ],
    transactions: [
      {
        date: "2025-04-20 14:32:15",
        currency: "BTC",
        type: "buy",
        price: 40000000,
        amount: 0.02,
        total: 800000,
        status: "완료"
      },
      {
        date: "2025-04-18 09:15:22",
        currency: "ETH",
        type: "buy",
        price: 2300000,
        amount: 0.5,
        total: 1150000,
        status: "완료"
      },
      {
        date: "2025-04-15 17:48:33",
        currency: "XRP",
        type: "sell",
        price: 600,
        amount: 500,
        total: 300000,
        status: "완료"
      },
      {
        date: "2025-04-10 11:20:45",
        currency: "BTC",
        type: "buy",
        price: 39500000,
        amount: 0.1,
        total: 3950000,
        status: "완료"
      }
    ]
  };

  return <MyInfo {...userData} />;
};

export default MyInfoPage;