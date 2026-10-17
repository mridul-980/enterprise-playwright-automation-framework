import { test, expect } from '../../../src/fixtures/testFixtures';

test('TC_012 - Navigate from cart to checkout', async ({
  page,
  loginPage,
  inventoryPage,
  cartPage,
  checkoutPage,
}) => {
  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await inventoryPage.addBackpackToCart();
  await page.locator('.shopping_cart_link').click();

  await expect(cartPage.checkoutButton).toBeVisible();
  await cartPage.checkoutButton.click();

  await expect(page).toHaveURL(/checkout-step-one\.html/);
  await expect(checkoutPage.checkoutTitle).toBeVisible();
  await expect(checkoutPage.firstNameInput).toBeVisible();
  await expect(checkoutPage.lastNameInput).toBeVisible();
  await expect(checkoutPage.postalCodeInput).toBeVisible();
});

test('TC_013 - Submit checkout information with valid details', async ({
  page,
  loginPage,
  inventoryPage,
  cartPage,
  checkoutPage,
}) => {
  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await inventoryPage.addBackpackToCart();
  await page.locator('.shopping_cart_link').click();
  await cartPage.checkoutButton.click();

  await checkoutPage.enterCheckoutInformation(
    'Mridul',
    'Tripathi',
    '226001'
  );
  await checkoutPage.continueToOverview();

  await expect(page).toHaveURL(/checkout-step-two\.html/);
  await expect(
    page.getByText('Sauce Labs Backpack', { exact: true })
  ).toBeVisible();
  await expect(page.getByText('Item total: $29.99')).toBeVisible();
});

test('TC_014 - Complete purchase and verify order confirmation', async ({
  page,
  loginPage,
  inventoryPage,
  cartPage,
  checkoutPage,
}) => {
  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await inventoryPage.addBackpackToCart();
  await page.locator('.shopping_cart_link').click();
  await cartPage.checkoutButton.click();

  await checkoutPage.enterCheckoutInformation(
    'Mridul',
    'Tripathi',
    '226001'
  );
  await checkoutPage.continueToOverview();

  await expect(page).toHaveURL(/checkout-step-two\.html/);
  await expect(page.getByText('Item total: $29.99')).toBeVisible();

  await checkoutPage.finishCheckout();

  await expect(page).toHaveURL(/checkout-complete\.html/);
  await expect(checkoutPage.confirmationMessage).toBeVisible();
  await expect(checkoutPage.backHomeButton).toBeVisible();
});