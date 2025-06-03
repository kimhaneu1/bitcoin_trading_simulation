import React from 'react';
import type { NextPage } from 'next';
import Head from 'next/head';
import LoginForm from '../components/LoginForm';
import SecurityAlertBanner from '../components/SecurityAlertBanner';

const Login: NextPage = () => {
  return (
    <div className="min-h-screen bg-gray-100">
      <Head>
        <title>로그인 | BiTS</title>
        <meta name="description" content="BiTS 암호화폐 거래소 로그인" />
        <link rel="icon" href="/favicon.ico" />
      </Head>

      {/* Security Alert Banner */}
      <div className="container mx-auto px-4">
        <SecurityAlertBanner className="my-4" />
      </div>

      {/* Signup Form */}
      <LoginForm />
    </div>
  );
};

export default Login;