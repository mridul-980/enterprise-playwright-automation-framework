
import { test, expect } from '@playwright/test';
import { LoginPage } from '../../../src/pages/LoginPage';

test('TC_001 - Login with valid credentials', async ({ page }) => {
  const loginPage = new LoginPage(page);

  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await expect(page).toHaveURL(/inventory.html/);
  await expect(
    page.getByText('Products', { exact: true })
  ).toBeVisible();
});


const invalidLoginCases = [
  {
    testId: 'TC_002',
    scenario: 'invalid username',
    username: 'invalid_user',
    password: 'secret_sauce',
    expectedError: 'Epic sadface: Username and password do not match any user in this service',
  },
  {
    testId: 'TC_003',
    scenario: 'invalid password',
    username: 'standard_user',
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
  test(`${testCase.testId} - Login with ${testCase.scenario}`, async ({ page }) => {
    const loginPage = new LoginPage(page);

    await loginPage.navigateTo('/');
    await loginPage.login(testCase.username, testCase.password);

    await expect(loginPage.getErrorMessage()).toHaveText(
  testCase.expectedError
);
    await expect(loginPage.getErrorMessage()).toBeVisible();
    await expect(loginPage.getErrorMessage()).toHaveText(
      testCase.expectedError
    );
  });
}

