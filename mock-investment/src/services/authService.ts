// 사용자 인증상태 관리 서비스
// API 호출 시 또는 로그인 상태 유지

class AuthService {
  private accessToken: string | null = null;
  private tokenKey = 'accessToken';
  private refreshTokenKey = 'refreshToken';

  constructor() {
    if (typeof window !== 'undefined') {
      this.accessToken = localStorage.getItem(this.tokenKey);
    }
  }

  setAccessToken(token: string) {
    this.accessToken = token;
    if (typeof window !== 'undefined') {
      localStorage.setItem(this.tokenKey, token);
    }
  }

  getAccessToken(): string | null {
    return this.accessToken;
  }

  setRefreshToken(token: string) {
    if (typeof window !== 'undefined') {
      localStorage.setItem(this.refreshTokenKey, token);
    }
  }

  getRefreshToken(): string | null {
    if (typeof window !== 'undefined') {
      return localStorage.getItem(this.refreshTokenKey);
    }
    return null;
  }

  clearTokens() {
    this.accessToken = null;
    if (typeof window !== 'undefined') {
      localStorage.removeItem(this.tokenKey);
      localStorage.removeItem(this.refreshTokenKey);
    }
  }

  async refreshAccessToken(): Promise<string> {
    const refreshToken = this.getRefreshToken();
    if (!refreshToken) {
      throw new Error('No refresh token available');
    }

    const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/auth/refresh`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ refreshToken })
    });

    if (!response.ok) {
      throw new Error('Token refresh failed');
    }

    const data = await response.json();
    this.setAccessToken(data.accessToken);
    this.setRefreshToken(data.refreshToken);

    return data.accessToken;
  }
}

export const authService = new AuthService();
