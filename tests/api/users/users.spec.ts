
import { test, expect } from '@playwright/test';
import { validateSchema } from '../../../src/api/helpers/schemaValidator';
import { userSchema } from '../../../src/api/schemas/user.schema';
import { UserApiClient } from '../../../src/api/clients/userApiClient';
import { newUserData, updatedUserData } from '../../../test-data/api/userTestData';
import { createdUserSchema } from '../../../src/api/schemas/createdUser.schema';
test('API_001 - Get user by ID', async ({ request }) => {
  const userApi = new UserApiClient(request);

  const response = await userApi.getUser(1);
  expect(response.status()).toBe(200);

  const responseBody: unknown = await response.json();

  // Validate response structure
  validateSchema(userSchema, responseBody);

  // Validate business data
  expect(responseBody).toMatchObject({
    id: 1,
    name: 'Leanne Graham',
    email: 'Sincere@april.biz',
  });
});


test('API_002 - Create a new user', async ({ request }) => {
  const userApi = new UserApiClient(request);
  const response = await userApi.createUser(newUserData);

  expect(response.status()).toBe(201);

  const responseBody = await response.json();
  validateSchema(createdUserSchema, responseBody);

  expect(responseBody.id).toBeTruthy();
  expect(responseBody.name).toBe(newUserData.name);
  expect(responseBody.username).toBe(newUserData.username);
  expect(responseBody.email).toBe(newUserData.email);
});


test('API_003 - Update an existing user', async ({ request }) => {
  const userApi = new UserApiClient(request);
  const response = await userApi.updateUser(1, updatedUserData);

  expect(response.status()).toBe(200);

  const responseBody = await response.json();

  expect(responseBody.id).toBe(1);
  expect(responseBody.name).toBe(updatedUserData.name);
  expect(responseBody.email).toBe(updatedUserData.email);
});


test('API_004 - Delete an existing user', async ({ request }) => {
  const userApi = new UserApiClient(request);

  const response = await userApi.deleteUser(1);

  expect(response.status()).toBe(200);
});

test('API_005 - Verify response for non-existent user', async ({ request }) => {
  const userApi = new UserApiClient(request);

  const response = await userApi.getUser(9999);

  expect(response.status()).toBe(404);
});

test('API_006 - Verify response content type', async ({ request }) => {
  const userApi = new UserApiClient(request);

  const response = await userApi.getUser(1);

  expect(response.status()).toBe(200);

  const contentType = response.headers()['content-type'];

  expect(contentType).toContain('application/json');
});
