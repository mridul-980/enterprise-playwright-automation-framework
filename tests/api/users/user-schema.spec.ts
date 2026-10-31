
import { test, expect } from '@playwright/test';
import { userSchema } from '../../../src/api/schemas/user.schema';
import { validateSchema } from '../../../src/api/helpers/schemaValidator';

const validUser = {
  id: 1,
  name: 'Leanne Graham',
  username: 'Bret',
  email: 'Sincere@april.biz',
  address: {
    street: 'Kulas Light',
    suite: 'Apt. 556',
    city: 'Gwenborough',
    zipcode: '92998-3874',
    geo: {
      lat: '-37.3159',
      lng: '81.1496',
    },
  },
  phone: '1-770-736-8031',
  website: 'hildegard.org',
  company: {
    name: 'Romaguera-Crona',
    catchPhrase: 'Multi-layered client-server neural-net',
    bs: 'harness real-time e-markets',
  },
};

test('SCHEMA_001 - Valid user response passes schema validation', async () => {
  expect(() => validateSchema(userSchema, validUser)).not.toThrow();
});

test('SCHEMA_002 - Missing required field fails validation', async () => {
  const invalidUser = {
    ...validUser,
    email: undefined,
  };

  expect(() => validateSchema(userSchema, invalidUser)).toThrow(
    'Schema validation failed'
  );
});

test('SCHEMA_003 - Incorrect data type fails validation', async () => {
  const invalidUser = {
    ...validUser,
    id: '1',
  };

  expect(() => validateSchema(userSchema, invalidUser)).toThrow(
    'Schema validation failed'
  );
});
