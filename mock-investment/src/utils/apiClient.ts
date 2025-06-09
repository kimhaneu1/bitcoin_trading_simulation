import { authService } from '@/services/authService';

class ApiClient {
  private baseURL: string;
  private isRefreshing = false;
  private refreshSubscribers: ((token: string) => void)[] = [];

  constructor(baseURL: string) {
    this.baseURL = baseURL;
  }

  private onTokenRefreshed(token: string) {
    this.refreshSubscribers.forEach(callback => callback(token));
    this.refreshSubscribers = [];
  }

  private addRefreshSubscriber(callback: (token: string) => void) {
    this.refreshSubscribers.push(callback);
  }

  private async handleTokenRefresh(): Promise<string> {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      try {
        const newToken = await authService.refreshAccessToken();
        this.isRefreshing = false;
        this.onTokenRefreshed(newToken);
        return newToken;
      } catch (error) {
        this.isRefreshing = false;
        authService.clearTokens();
        window.location.href = '/login';
        throw error;
      }
    }
    return new Promise(resolve => {
      this.addRefreshSubscriber(resolve);
    });
  }

  async request<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
    const accessToken = authService.getAccessToken();
    const config: RequestInit = {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        ...(accessToken && { Authorization: `Bearer ${accessToken}` }),
        ...options.headers
      },
      credentials: 'include'
    };

    let response = await fetch(`${this.baseURL}${endpoint}`, config);

    if (response.status === 401 && accessToken) {
      const newToken = await this.handleTokenRefresh();
      config.headers = {
        ...config.headers,
        Authorization: `Bearer ${newToken}`
      };
      response = await fetch(`${this.baseURL}${endpoint}`, config);
    }

    if (!response.ok) {
      throw new Error(`API call failed: ${response.statusText}`);
    }

    return response.json();
  }

  get<T>(endpoint: string): Promise<T> {
    return this.request<T>(endpoint, { method: 'GET' });
  }

  post<T>(endpoint: string, data?: any): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'POST',
      body: data ? JSON.stringify(data) : undefined
    });
  }

  put<T>(endpoint: string, data?: any): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'PUT',
      body: data ? JSON.stringify(data) : undefined
    });
  }

  delete<T>(endpoint: string): Promise<T> {
    return this.request<T>(endpoint, { method: 'DELETE' });
  }
}

export const apiClient = new ApiClient(
  process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api'
);
