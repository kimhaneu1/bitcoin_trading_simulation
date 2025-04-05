import { ReactNode } from 'react';
import Link from 'next/link';

interface LayoutProps {
  children: ReactNode;
}

const Layout = ({ children }: LayoutProps) => {
  return (
    <div className="min-h-screen bg-gray-900 text-white">
      {/* 헤더 */}
      <header className="bg-gray-800 border-b border-gray-700">
        <div className="container mx-auto px-4 py-4 flex justify-between items-center">
          <Link href="/" className="text-xl font-bold">
            Mock Investment
          </Link>
          
          <nav>
            <ul className="flex space-x-4">
              <li>
                <Link href="/login" className="px-4 py-2 rounded hover:bg-gray-700">
                  로그인
                </Link>
              </li>
              <li>
                <Link href="/register" className="px-4 py-2 bg-blue-600 rounded hover:bg-blue-700">
                  회원가입
                </Link>
              </li>
            </ul>
          </nav>
        </div>
      </header>
      
      {/* 메인 컨텐츠 */}
      <main className="flex-grow">
        {children}
      </main>
      
      {/* 푸터 */}
      <footer className="bg-gray-800 border-t border-gray-700 py-6">
        <div className="container mx-auto px-4 text-center text-gray-400 text-sm">
          <p>© 2025 Mock Investment. 실제 투자와는 무관합니다.</p>
        </div>
      </footer>
    </div>
  );
};

export default Layout;