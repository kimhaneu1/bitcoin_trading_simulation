import React, { useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/router';
import Header from './Header';

const LoginForm: React.FC = () => {
  const router = useRouter();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [keepLoggedIn, setKeepLoggedIn] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!email || !password) {
      setError('이메일과 비밀번호를 입력해주세요.');
      return;
    }

    try {
      console.log('Logging in:', { email, password, keepLoggedIn });
      router.push('/myinfo');
    } catch (err) {
      setError('로그인 중 오류가 발생했습니다. 다시 시도해주세요.');
    }
  };

  return (
    <div className="bg-white min-h-screen">
      <Header />
      <div className="flex justify-center items-center min-h-screen bg-white">
        <div className="w-full max-w-sm p-6 bg-white rounded-lg">
          <div className="flex justify-center mb-8">
            <img src="/images/logo.png" alt="BiTS Logo" className="h-12" />
          </div>
          
          <h2 className="text-2xl font-bold text-center text-gray-800 mb-6">로그인</h2>
          <p className="text-center text-gray-600 mb-4">로그인하여 거래소를 이용하세요</p>

          {/* Error message */}
          {error && (
            <div className="mb-4 p-3 bg-red-100 text-red-600 rounded">{error}</div>
          )}

          <form onSubmit={handleSubmit}>
            <div className="mb-4">
              <label htmlFor="email" className="block text-gray-700 mb-1">이메일</label>
              <input
                id="email"
                type="email"
                className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none"
                placeholder="이메일을 입력하세요"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>

            <div className="mb-4">
              <label htmlFor="password" className="block text-gray-700 mb-1">비밀번호</label>
              <input
                id="password"
                type="password"
                className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none"
                placeholder="비밀번호를 입력하세요"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>

            <div className="flex justify-between items-center mb-6">
              <label className="flex items-center">
                <input
                  type="checkbox"
                  className="mr-2"
                  checked={keepLoggedIn}
                  onChange={() => setKeepLoggedIn(!keepLoggedIn)}
                />
                로그인 상태 유지
              </label>
              <Link href="#" className="text-sm text-sky-400 hover:underline">
                비밀번호 찾기
              </Link>
            </div>

            <button
              type="submit"
              className="w-full bg-sky-300 hover:bg-sky-400 text-white font-bold py-2 px-4 rounded-lg"
            >
              로그인
            </button>
          </form>

          <p className="text-center text-sm text-gray-600 mt-6">
            계정이 없으신가요?{' '}
            <Link href="/signup" className="text-sky-400 hover:underline">회원가입</Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default LoginForm;
