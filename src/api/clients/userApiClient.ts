
import { APIRequestContext, APIResponse } from '@playwright/test';
import {
  CreateUserRequest,
  UpdateUserRequest,
} from '../types/user.types';

export class UserApiClient {
  constructor(private readonly request: APIRequestContext) {}

  getUser(userId: number): Promise<APIResponse> {
    return this.request.get(`/users/${userId}`);
  }

  createUser(userData: CreateUserRequest): Promise<APIResponse> {
    return this.request.post('/users', {
      data: userData,
    });
  }

  updateUser(
    userId: number,
    userData: UpdateUserRequest
  ): Promise<APIResponse> {
    return this.request.put(`/users/${userId}`, {
      data: userData,
    });
  }

  deleteUser(userId: number): Promise<APIResponse> {
    return this.request.delete(`/users/${userId}`);
  }
}
