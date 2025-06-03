import type { NextPage } from 'next';
import Head from 'next/head';
import React from 'react';
import RankPage from '../components/RankPage';

const Rank: NextPage = () => {
  return (
    <div className="min-h-screen bg-gray-100">
      <Head>
        <title>순위 | BiTS</title>
        <meta name="description" content="BiTS 암호화폐 거래소 순위 페이지" />
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <RankPage />
    </div>
  );
};

export default Rank;
