
import { test, expect } from '@playwright/test';
import { LoginPage } from '../../../src/pages/LoginPage';
import { validUser, invalidUser } from '../../../test-data/credentials';

test('TC_001 - Login with valid credentials @smoke @regression', async ({ page }) => {
  const loginPage = new LoginPage(page);

  await loginPage.navigateTo('/');
  await loginPage.login(validUser.username, validUser.password);

  await expect(page).toHaveURL(/inventory.html/);
  await expect(
    page.getByText('Products', { exact: true })
  ).toBeVisible();
});


const invalidLoginCases = [
  {
    testId: 'TC_002',
    scenario: 'invalid username',
    username: invalidUser.username,
    password: invalidUser.password,
    expectedError: 'Epic sadface: Username and password do not match any user in this service',
  },
  {
    testId: 'TC_003',
    scenario: 'invalid password',
    username: validUser.username,
    password: 'wrong_password',
    expectedError: 'Epic sadface: Username and password do not match any user in this service',
  },
  {
  testId: 'TC_004',
  scenario: 'empty credentials',
  username: '',
  password: '',
  expectedError: 'Epic sadface: Username is required',
},
];

for (const testCase of invalidLoginCases) {
  test(`${testCase.testId} - Login with ${testCase.scenario} @regression`, async ({ page }) => {
    const loginPage = new LoginPage(page);

    await loginPage.navigateTo('/');
    await loginPage.login(testCase.username, testCase.password);

  const errorMessage = loginPage.getErrorMessage();

await expect(errorMessage).toBeVisible();
await expect(errorMessage).toHaveText(testCase.expectedError);
  });
}

