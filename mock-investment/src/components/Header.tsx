import React from 'react';
import Link from 'next/link';

export default function Header() {
  return (
    <header className="bg-blue-100 p-4 flex justify-between items-center">
      <a href="/"><h1 className="text-xl font-bold text-blue-700" style={{ width: '3em' }} ><img src="/images/logo_primary.png"/></h1></a>
      <nav className="space-x-6 text-sm text-gray-700">
        <a href="/">거래소</a>
        <Link href="/myinfo">내정보</Link>
        <Link href="/rank">랭킹</Link>
      </nav>
      <div className="space-x-2">
        <button className="text-sm text-blue-600"><Link href="/login">로그인</Link></button>
        <button className="text-sm text-blue-600"><Link href="/signup">회원가입</Link></button>
      </div>
    </header>
  );
}