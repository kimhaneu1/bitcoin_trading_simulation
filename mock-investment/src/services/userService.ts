import { apiClient } from '@/utils/apiClient';

interface SignupRequest {
  email: string;
  password: string;
  username: string;
  phoneNumber: string;
}

interface SignupResponse {
  id: number;
  email: string;
  username: string;
  phoneNumber: string;
  registeredAt: string;
}

class UserService {
  async signup(data: SignupRequest): Promise<SignupResponse> {
    return apiClient.post<SignupResponse>('/users', data);
  }

  async getUserByEmail(email: string): Promise<SignupResponse> {
    return apiClient.get<SignupResponse>(`/users/email/${email}`);
  }

  async deleteUser(email: string): Promise<void> {
    await apiClient.delete<void>(`/users/${email}`);
  }
}

export const userService = new UserService();
