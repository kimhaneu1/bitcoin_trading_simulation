// src/components/RankPage.tsx
import React from 'react';
import Image from 'next/image';
import { rankData } from '../mock/rankData';
import Header from './Header';

const RankPage: React.FC = () => {
  return (
    <div className="bg-white min-h-screen">
      <Header />
    <div className="bg-white min-h-screen">
      <div className="relative w-full h-56">
        <Image src="/images/rank_banner.png" alt="Rank Banner" layout="fill" objectFit="cover" />
      </div>

      <div className="max-w-5xl mx-auto px-4 py-10">
        <h2 className="text-3xl font-bold text-center mb-6 text-purple-700">
          제 1회 비트코인 모의투자 대회
        </h2>

        <div className="overflow-x-auto rounded-lg shadow">
          <table className="w-full text-center border-collapse">
            <thead>
              <tr className="bg-gradient-to-r from-indigo-200 to-purple-200">
                <th className="py-3 px-4">순위</th>
                <th className="py-3 px-4">이름</th>
                <th className="py-3 px-4">보유자산</th>
                <th className="py-3 px-4">보유 코인</th>
                <th className="py-3 px-4">수익률</th>
              </tr>
            </thead>
            <tbody>
              {rankData.map((user, index) => (
                <tr key={index} className="border-b hover:bg-gray-50">
                  <td className="py-2 px-4 font-semibold">
                    {index === 0 ? '👑' : index + 1}
                  </td>
                  <td className="py-2 px-4">{user.name}</td>
                  <td className="py-2 px-4">{user.asset}</td>
                  <td className="py-2 px-4">{user.coin}</td>
                  <td className="py-2 px-4 text-green-600">{user.return}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        <div className="mt-12">
          <h3 className="text-xl font-bold mb-4">댓글 (15163)</h3>
          <div className="text-sm text-gray-600 mb-2">
            댓글 작성 시 IP가 기록되며 사이트 이용 제한이나 요청에 따라 법적 조치가 취해질 수 있습니다.
          </div>
          <form className="flex items-center space-x-4 mb-6">
            <input
              type="text"
              placeholder="닉네임"
              className="border px-3 py-2 rounded w-1/4"
            />
            <input
              type="password"
              placeholder="비밀번호"
              className="border px-3 py-2 rounded w-1/4"
            />
            <button className="bg-purple-500 text-white px-4 py-2 rounded">확인하기</button>
          </form>

          <div className="space-y-3">
            <div className="p-3 bg-gray-100 rounded">
              <p className="text-sm font-semibold">덱스</p>
              <p>하이</p>
            </div>
            <div className="p-3 bg-gray-100 rounded">
              <p className="text-sm font-semibold">홍진호</p>
              <p>이거 어떻게 하는거임?</p>
            </div>
            <div className="p-3 bg-gray-100 rounded">
              <p className="text-sm font-semibold">유리사</p>
              <p>내가 알려줄게 ㅋㅋㅋㅋ</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    </div>
  );
};

export default RankPage;
