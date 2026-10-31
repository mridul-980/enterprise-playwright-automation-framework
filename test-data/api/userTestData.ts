import {
  CreateUserRequest,
  UpdateUserRequest,
} from '../../src/api/types/user.types';

export const newUserData: CreateUserRequest = {
  name: 'Mridul Tripathi',
  username: 'mridul',
  email: 'mridul@example.com',
};

export const updatedUserData: UpdateUserRequest = {
  name: 'Mridul Updated',
  email: 'mridul.updated@example.com',
};