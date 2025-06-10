import React from 'react';
import type { NextPage } from 'next';
import Head from 'next/head';
import SignupForm from '../components/SignupForm';
import ServerDebugPanel from './serverDebugPanel';
import SecurityAlertBanner from '../components/SecurityAlertBanner';

const Signup: NextPage = () => {
  return (
    <div className="min-h-screen bg-gray-100">
      <Head>
        <title>회원가입 | BiTS</title>
        <meta name="description" content="BiTS 암호화폐 거래소 회원가입" />
        <link rel="icon" href="/favicon.ico" />
      </Head>

      {/* Security Alert Banner */}
      <div className="container mx-auto px-4">
        <SecurityAlertBanner className="my-4" />
      </div>

      {/* Signup Form */}
      <SignupForm />
      <ServerDebugPanel/>
    </div>
  );
};

export default Signup;