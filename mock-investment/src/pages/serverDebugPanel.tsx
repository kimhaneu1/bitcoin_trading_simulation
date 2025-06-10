import React, { useState, useEffect } from 'react';
import { apiClient } from '@/utils/apiClient';

interface ServerStatus {
  status: 'checking' | 'online' | 'offline' | 'cors-error';
  message: string;
  details?: any;
  lastChecked?: Date;
}

interface ApiTestResult {
  endpoint: string;
  method: string;
  status: 'success' | 'error' | 'pending';
  response?: any;
  error?: string;
  duration?: number;
  statusCode?: number;
}

interface SignupTestData {
  email: string;
  password: string;
  username: string;
  phoneNumber: string;
}

const ServerDebugPanel: React.FC = () => {
  const [serverStatus, setServerStatus] = useState<ServerStatus>({
    status: 'checking',
    message: 'Checking server status...'
  });
  
  const [isVisible, setIsVisible] = useState(false);
  const [testResults, setTestResults] = useState<ApiTestResult[]>([]);
  const [customSignupData, setCustomSignupData] = useState<SignupTestData>({
    email: 'test@ex.com', // 25글자 이하
    password: 'Pass123!', // 20글자 이하, 숫자1개+특수문자1개
    username: 'user1', // 10글자 이하
    phoneNumber: '010-1234-5678' // 최소 10글자
  });

  const [corsProxy, setCorsProxy] = useState('');

  useEffect(() => {
    checkServerStatus();
  }, []);

  const checkServerStatus = async () => {
    setServerStatus({ status: 'checking', message: 'Checking server status...' });
    
    try {
      const startTime = Date.now();
      
      // 의도적으로 실패할 회원가입 요청으로 서버 상태 확인
      const response = await fetch(`${corsProxy}${process.env.NEXT_PUBLIC_API_URL || 'http://13.125.210.125:8080'}/api/users`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email: 'status-check@invalid.com',
          password: 'invalid',
          username: 'invalid',
          phoneNumber: 'invalid'
        }),
        mode: corsProxy ? 'cors' : 'cors'
      });
      
      const duration = Date.now() - startTime;
      
      // 400이나 409는 서버가 살아있음을 의미 (정상 응답)
      if (response.status === 400 || response.status === 409 || response.status === 422) {
        setServerStatus({
          status: 'online',
          message: `Server is online (${duration}ms) - API responding`,
          lastChecked: new Date()
        });
      } else if (response.status === 403) {
        setServerStatus({
          status: 'online',
          message: `Server online but CORS/Auth issues (${duration}ms)`,
          lastChecked: new Date()
        });
      } else {
        setServerStatus({
          status: 'offline',
          message: `Server responded with ${response.status}: ${response.statusText}`,
          lastChecked: new Date()
        });
      }
    } catch (error: any) {
      let status: ServerStatus['status'] = 'offline';
      let message = 'Server is offline';
      
      if (error.message?.includes('CORS')) {
        status = 'cors-error';
        message = 'CORS error - try using CORS proxy';
      } else if (error.message?.includes('fetch')) {
        message = 'Network error - server may be down';
      }
      
      setServerStatus({
        status,
        message: `${message}: ${error.message}`,
        details: error,
        lastChecked: new Date()
      });
    }
  };

  const testApiEndpoint = async (endpoint: string, method: string = 'GET', data?: any, description?: string) => {
    const testId = `${method}-${endpoint}`;
    
    setTestResults(prev => [
      ...prev.filter(r => r.endpoint !== endpoint || r.method !== method),
      { endpoint, method, status: 'pending' }
    ]);

    try {
      const startTime = Date.now();
      const url = `${corsProxy}${process.env.NEXT_PUBLIC_API_URL || 'http://13.125.210.125:8080'}${endpoint}`;
      
      console.log(`🚀 Testing ${method} ${endpoint}`, data ? { data } : '');
      
      const response = await fetch(url, {
        method,
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          // CORS 우회를 위한 추가 헤더
          'X-Requested-With': 'XMLHttpRequest',
        },
        body: method !== 'GET' && data ? JSON.stringify(data) : undefined,
        mode: 'cors',
        credentials: 'omit' // 인증 정보 제외하고 테스트
      });
      
      const duration = Date.now() - startTime;
      
      let responseData;
      const contentType = response.headers.get('content-type');
      
      try {
        if (contentType && contentType.includes('application/json')) {
          responseData = await response.json();
        } else {
          responseData = await response.text();
        }
      } catch {
        responseData = 'No response body';
      }

      console.log(`📥 Response ${response.status}:`, responseData);
      
      if (response.ok) {
        setTestResults(prev => [
          ...prev.filter(r => r.endpoint !== endpoint || r.method !== method),
          {
            endpoint,
            method,
            status: 'success',
            response: responseData,
            duration,
            statusCode: response.status
          }
        ]);
      } else {
        setTestResults(prev => [
          ...prev.filter(r => r.endpoint !== endpoint || r.method !== method),
          {
            endpoint,
            method,
            status: 'error',
            error: `${response.status} ${response.statusText}`,
            response: responseData,
            duration,
            statusCode: response.status
          }
        ]);
      }
    } catch (error: any) {
      console.error(`💥 ${method} ${endpoint} failed:`, error);
      
      setTestResults(prev => [
        ...prev.filter(r => r.endpoint !== endpoint || r.method !== method),
        {
          endpoint,
          method,
          status: 'error',
          error: error.message,
          response: error.responseBody
        }
      ]);
    }
  };

  const testCompleteSignupFlow = async () => {
    console.log('🔄 Testing complete signup + login flow...');
    
    // 백엔드 검증 규칙에 맞는 새로운 랜덤 데이터 생성
    const timestamp = Date.now();
    const testData = {
      username: `user${String(timestamp).slice(-4)}`, // 10글자 이하
      password: 'Pass123!', // 20글자 이하, 숫자+특수문자 포함
      email: `test${timestamp}@ex.com`, // 25글자 이하
      phoneNumber: `010-${String(Math.floor(1000 + Math.random() * 9000))}-${String(Math.floor(1000 + Math.random() * 9000))}` // 최소 10글자
    };
    
    // 2. 회원가입
    await testApiEndpoint('/api/users', 'POST', testData, '회원가입');
    
    // 3. 2초 후 로그인 시도
    setTimeout(async () => {
      await testApiEndpoint('/api/auth/login', 'POST', {
        email: testData.email,
        password: testData.password
      }, '로그인');
    }, 2000);
  };

  const generateRandomTestData = () => {
    const timestamp = Date.now();
    // 백엔드 검증 규칙에 맞는 데이터 생성
    const phoneNumber = `010-${String(Math.floor(1000 + Math.random() * 9000))}-${String(Math.floor(1000 + Math.random() * 9000))}`;
    
    setCustomSignupData({
      email: `test${timestamp}@ex.com`, // 25글자 이하
      password: 'Pass123!', // 20글자 이하, 숫자1개+특수문자1개 포함
      username: `user${String(timestamp).slice(-4)}`, // 10글자 이하
      phoneNumber: phoneNumber // 최소 10글자 (010-XXXX-XXXX = 13글자)
    });
  };

  const getStatusColor = (status: ServerStatus['status']) => {
    switch (status) {
      case 'online': return 'bg-green-100 border-green-400 text-green-700';
      case 'offline': return 'bg-red-100 border-red-400 text-red-700';
      case 'cors-error': return 'bg-yellow-100 border-yellow-400 text-yellow-700';
      default: return 'bg-blue-100 border-blue-400 text-blue-700';
    }
  };

  const getTestStatusColor = (status: ApiTestResult['status']) => {
    switch (status) {
      case 'success': return 'text-green-600';
      case 'error': return 'text-red-600';
      default: return 'text-blue-600';
    }
  };

  const getTestStatusIcon = (result: ApiTestResult) => {
    if (result.status === 'pending') return '🔄';
    if (result.status === 'success') return '✅';
    if (result.statusCode === 409) return '⚠️'; // Duplicate
    if (result.statusCode === 400) return '🔸'; // Validation error
    if (result.statusCode === 403) return '🔒'; // Forbidden
    if (result.statusCode === 404) return '❓'; // Not found
    if (result.statusCode === 500) return '💥'; // Server error
    return '❌';
  };

  const getSuccessMessage = (result: ApiTestResult) => {
    if (result.statusCode === 201) return '회원가입 성공!';
    if (result.statusCode === 200) return '요청 성공!';
    if (result.statusCode === 409) return '중복 데이터 (정상 검증)';
    if (result.statusCode === 400) return '입력값 검증 오류';
    if (result.statusCode === 403) return 'CORS/권한 문제';
    if (result.statusCode === 500) return '서버 내부 오류';
    return '응답 받음';
  };

  if (!isVisible) {
    return (
      <button
        onClick={() => setIsVisible(true)}
        className="fixed bottom-4 right-4 bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-lg shadow-lg z-50"
      >
        🔧 회원가입 테스트
      </button>
    );
  }

  return (
    <div className="fixed bottom-4 right-4 w-[520px] max-h-[600px] overflow-auto bg-white border border-gray-300 rounded-lg shadow-xl z-50">
      <div className="p-4">
        <div className="flex justify-between items-center mb-4">
          <h3 className="text-lg font-semibold">회원가입 테스트 패널</h3>
          <button
            onClick={() => setIsVisible(false)}
            className="text-gray-500 hover:text-gray-700"
          >
            ✕
          </button>
        </div>

        {/* Server Status */}
        <div className={`p-3 mb-4 border rounded ${getStatusColor(serverStatus.status)}`}>
          <div className="flex justify-between items-center">
            <span className="font-medium">
              {serverStatus.status === 'checking' ? '🔄' : 
               serverStatus.status === 'online' ? '✅' : 
               serverStatus.status === 'cors-error' ? '⚠️' : '❌'}
              {serverStatus.message}
            </span>
            <button
              onClick={checkServerStatus}
              className="text-sm px-2 py-1 bg-white rounded border hover:bg-gray-50"
            >
              Refresh
            </button>
          </div>
          {serverStatus.lastChecked && (
            <div className="text-xs mt-1">
              Last checked: {serverStatus.lastChecked.toLocaleTimeString()}
            </div>
          )}
        </div>

        {/* Validation Rules Guide */}
        <div className="mb-4 p-3 bg-blue-50 border border-blue-200 rounded">
          <h4 className="font-medium text-sm mb-2">📋 백엔드 검증 규칙</h4>
          <div className="text-xs space-y-1">
            <div>• 이메일: 25글자 이하</div>
            <div>• 비밀번호: 20글자 이하, 숫자1개+특수문자1개 포함</div>
            <div>• 이름: 10글자 이하</div>
            <div>• 휴대폰: 최소 10글자 (010-XXXX-XXXX)</div>
          </div>
        </div>

        {/* Test Data */}
        <div className="mb-4">
          <div className="flex justify-between items-center mb-2">
            <h4 className="font-medium">테스트 데이터</h4>
            <button
              onClick={generateRandomTestData}
              className="text-xs px-2 py-1 bg-blue-100 hover:bg-blue-200 rounded"
            >
              🎲 새 데이터
            </button>
          </div>
          <div className="grid grid-cols-2 gap-2 text-xs">
            <input
              type="email"
              placeholder="Email (25글자 이하)"
              value={customSignupData.email}
              onChange={(e) => setCustomSignupData({...customSignupData, email: e.target.value})}
              className="px-2 py-1 border rounded"
              maxLength={25}
            />
            <input
              type="text"
              placeholder="Username (10글자 이하)"
              value={customSignupData.username}
              onChange={(e) => setCustomSignupData({...customSignupData, username: e.target.value})}
              className="px-2 py-1 border rounded"
              maxLength={10}
            />
            <input
              type="text"
              placeholder="Phone (최소 10글자)"
              value={customSignupData.phoneNumber}
              onChange={(e) => setCustomSignupData({...customSignupData, phoneNumber: e.target.value})}
              className="px-2 py-1 border rounded"
              minLength={10}
            />
            <input
              type="text"
              placeholder="Password (20글자, 숫자+특수문자)"
              value={customSignupData.password}
              onChange={(e) => setCustomSignupData({...customSignupData, password: e.target.value})}
              className="px-2 py-1 border rounded"
              maxLength={20}
            />
          </div>
        </div>

        {/* API Tests */}
        <div className="mb-4">
          <h4 className="font-medium mb-2">API 테스트</h4>
          <div className="space-y-2">
            <button
              onClick={() => testApiEndpoint('/api/users', 'POST', customSignupData)}
              className="w-full text-left px-3 py-2 bg-green-100 hover:bg-green-200 rounded text-sm"
            >
              👤 회원가입 테스트
            </button>
            <button
              onClick={() => testApiEndpoint('/api/auth/login', 'POST', {
                email: customSignupData.email,
                password: customSignupData.password
              })}
              className="w-full text-left px-3 py-2 bg-blue-100 hover:bg-blue-200 rounded text-sm"
            >
              🔑 로그인 테스트
            </button>
            <button
              onClick={testCompleteSignupFlow}
              className="w-full text-left px-3 py-2 bg-purple-100 hover:bg-purple-200 rounded text-sm font-medium"
            >
              🚀 전체 플로우 (회원가입 → 로그인)
            </button>
          </div>
        </div>

        {/* Test Results */}
        {testResults.length > 0 && (
          <div>
            <h4 className="font-medium mb-2">테스트 결과</h4>
            <div className="space-y-2 max-h-60 overflow-auto">
              {testResults.map((result, index) => (
                <div key={index} className="text-xs border rounded p-3">
                  <div className={`font-medium ${getTestStatusColor(result.status)} flex items-center justify-between`}>
                    <span>
                      {getTestStatusIcon(result)} {result.method} {result.endpoint}
                    </span>
                    {result.duration && (
                      <span className="text-gray-500">({result.duration}ms)</span>
                    )}
                  </div>
                  
                  {result.statusCode && (
                    <div className="mt-1 flex items-center gap-2">
                      <span className={`px-2 py-1 rounded text-xs ${
                        result.statusCode >= 200 && result.statusCode < 300 ? 'bg-green-100 text-green-800' :
                        result.statusCode === 409 ? 'bg-yellow-100 text-yellow-800' :
                        result.statusCode === 400 ? 'bg-orange-100 text-orange-800' :
                        'bg-red-100 text-red-800'
                      }`}>
                        {result.statusCode}
                      </span>
                      <span className="text-gray-600">{getSuccessMessage(result)}</span>
                    </div>
                  )}
                  
                  {result.error && (
                    <div className="text-red-600 mt-1 text-xs">
                      {result.error}
                    </div>
                  )}
                  
                  {result.response && (
                    <details className="mt-2">
                      <summary className="cursor-pointer text-gray-600 hover:text-gray-800 text-xs">
                        📄 응답 내용 보기
                      </summary>
                      <pre className="mt-1 p-2 bg-gray-100 rounded text-xs overflow-auto max-h-32">
                        {typeof result.response === 'string' 
                          ? result.response 
                          : JSON.stringify(result.response, null, 2)}
                      </pre>
                    </details>
                  )}
                </div>
              ))}
            </div>
            <button
              onClick={() => setTestResults([])}
              className="w-full mt-2 px-3 py-1 bg-gray-100 hover:bg-gray-200 rounded text-sm"
            >
              결과 지우기
            </button>
          </div>
        )}

        {/* Success Guide */}
        <div className="mt-4 pt-4 border-t text-xs text-gray-600">
          <div className="mb-2"><strong>성공 기준:</strong></div>
          <div>✅ 201: 회원가입 성공</div>
          <div>⚠️ 409: 중복 데이터 (정상)</div>
          <div>🔒 403: CORS/권한 문제</div>
          <div className="mt-2">
            <strong>API URL:</strong> {process.env.NEXT_PUBLIC_API_URL || 'http://13.125.210.125:8080'}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ServerDebugPanel;