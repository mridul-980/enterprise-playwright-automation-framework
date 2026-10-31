
import { test, expect } from '@playwright/test';
import { validateSchema } from '../../../src/api/helpers/schemaValidator';
import { userSchema } from '../../../src/api/schemas/user.schema';


test('API_001 - Get user by ID', async ({ request }) => {

  const response = await request.get('/users/1');
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

  const response = await request.post(
    '/users',
    {
      data: {
        name: 'Mridul Tripathi',
        username: 'mridul',
        email: 'mridul@example.com',
      },
    }
  );

  expect(response.status()).toBe(201);

  const responseBody = await response.json();

  expect(responseBody.id).toBeTruthy();
  expect(responseBody.name).toBe('Mridul Tripathi');
  expect(responseBody.username).toBe('mridul');
  expect(responseBody.email).toBe('mridul@example.com');
});


test('API_003 - Update an existing user', async ({ request }) => {

  const response = await request.put(
    '/users/1',
    {
      data: {
        name: 'Mridul Updated',
        email: 'mridul.updated@example.com',
      },
    }
  );

  expect(response.status()).toBe(200);

  const responseBody = await response.json();

  expect(responseBody.id).toBe(1);
  expect(responseBody.name).toBe('Mridul Updated');
  expect(responseBody.email).toBe('mridul.updated@example.com');
});


test('API_004 - Delete an existing user', async ({ request }) => {

  const response = await request.delete(
    '/users/1'
  );

  expect(response.status()).toBe(200);
});

test('API_005 - Verify response for non-existent user', async ({ request }) => {

  const response = await request.get(
    '/users/9999'
  );

  expect(response.status()).toBe(404);
});

test('API_006 - Verify response content type', async ({ request }) => {

  const response = await request.get(
    '/users/1'
  );

  expect(response.status()).toBe(200);

  const contentType = response.headers()['content-type'];

  expect(contentType).toContain('application/json');
});
