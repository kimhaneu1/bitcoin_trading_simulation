import { useEffect, useState } from "react";
import { Geist, Geist_Mono } from "next/font/google";
import TickerCard from "@/components/TickerCard";
import Layout from "@/components/Layout";
import CandleChart from "@/components/CandleChart";
import { api } from "@/lib/api";
import { TickerData } from "@/lib/types";

// Geist Sans 폰트 불러오기 (일반 텍스트용)
const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

// Geist Mono 폰트 불러오기 (코드/숫자용)
const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export default function Home() {
  // 상태 관리
  const [ticker, setTicker] = useState<TickerData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedMarket, setSelectedMarket] = useState("KRW-BTC");

  // 티커 데이터 불러오기
  const fetchTicker = async (market: string) => {
    try {
      setLoading(true);
      setError(null);
      const response = await api.getTicker(market);
      setTicker(response.data);
    } catch (err) {
      console.error("Failed to fetch ticker:", err);
      setError("시세 정보를 불러오는데 실패했습니다");
    } finally {
      setLoading(false);
    }
  };

  // 마운트 시 초기 데이터 로드
  useEffect(() => {
    fetchTicker(selectedMarket);

    // 10초마다 데이터 갱신
    const intervalId = setInterval(() => {
      fetchTicker(selectedMarket);
    }, 10000);

    return () => clearInterval(intervalId);
  }, [selectedMarket]);

  // 마켓 변경 핸들러
  const handleMarketChange = (market: string) => {
    setSelectedMarket(market);
  };

  return (
    <div
      className={`min-h-screen ${geistSans.variable} ${geistMono.variable} font-sans`}
    >
      <Layout>
        <div className="container mx-auto px-4 py-8">
          <h1 className="text-2xl font-bold mb-6">비트코인 모의투자</h1>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            {/* 왼쪽 사이드바 - 마켓 선택 */}
            <div className="md:col-span-1">
              <h2 className="text-xl font-semibold mb-4">마켓 정보</h2>
              <div className="space-y-4">
                {/* 마켓 선택 버튼 */}
                <div className="flex flex-wrap gap-2">
                  {["KRW-BTC", "KRW-ETH", "KRW-XRP"].map((market) => (
                    <button
                      key={market}
                      className={`px-3 py-1 rounded text-sm ${
                        selectedMarket === market
                          ? "bg-blue-600 text-white"
                          : "bg-gray-700 text-gray-300"
                      }`}
                      onClick={() => handleMarketChange(market)}
                    >
                      {market.split("-")[1]}
                    </button>
                  ))}
                </div>

                {/* 티커 카드 */}
                {loading ? (
                  <div className="animate-pulse p-4 bg-gray-800 rounded">
                    <div className="h-4 bg-gray-700 rounded w-3/4 mb-2"></div>
                    <div className="h-4 bg-gray-700 rounded w-1/2"></div>
                  </div>
                ) : error ? (
                  <div className="p-4 bg-red-900 text-white rounded">
                    {error}
                  </div>
                ) : ticker ? (
                  <TickerCard
                    market={ticker.market}
                    tradePrice={ticker.trade_price}
                    changeRate={ticker.signed_change_rate}
                  />
                ) : null}
              </div>
            </div>

            {/* 메인 컨텐츠 - 차트 */}
            <div className="md:col-span-2">
              <h2 className="text-xl font-semibold mb-4">차트</h2>
              <CandleChart symbol={selectedMarket} timeframe="days" />
            </div>
          </div>
        </div>
      </Layout>
    </div>
  );
}