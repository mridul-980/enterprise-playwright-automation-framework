export interface CreateUserRequest {
  name: string;
  username: string;
  email: string;
}

export interface UpdateUserRequest {
  name: string;
  email: string;
}