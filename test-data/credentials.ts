
type UserCredentials = {
  username: string;
  password: string;
};

export const validUser: UserCredentials = {
  username: 'standard_user',
  password: 'secret_sauce',
};

export const invalidUser: UserCredentials = {
  username: 'invalid_user',
  password: 'secret_sauce',
};
