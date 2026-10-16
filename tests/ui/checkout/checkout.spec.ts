import { test, expect } from '@playwright/test';
import { LoginPage } from '../../../src/pages/LoginPage';
import { InventoryPage } from '../../../src/pages/InventoryPage';
import { CartPage } from '../../../src/pages/CartPage';
import { CheckoutPage } from '../../../src/pages/CheckoutPage';

test('TC_012 - Navigate from cart to checkout', async ({ page }) => {
  const loginPage = new LoginPage(page);
  const inventoryPage = new InventoryPage(page);
  const cartPage = new CartPage(page);
  const checkoutPage = new CheckoutPage(page);

  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await inventoryPage.addBackpackToCart();
  await page.locator('.shopping_cart_link').click();

  await expect(cartPage.checkoutButton).toBeVisible();
  await cartPage.checkoutButton.click();

  await expect(page).toHaveURL(/checkout-step-one\.html/);
  await expect(checkoutPage.checkoutTitle).toBeVisible();

  // Verify the checkout form is available
  await expect(checkoutPage.firstNameInput).toBeVisible();
  await expect(checkoutPage.lastNameInput).toBeVisible();
  await expect(checkoutPage.postalCodeInput).toBeVisible();
});

test('TC_013 - Submit checkout information with valid details', async ({ page }) => {
  const loginPage = new LoginPage(page);
  const inventoryPage = new InventoryPage(page);
  const cartPage = new CartPage(page);
  const checkoutPage = new CheckoutPage(page);

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

  // Verify navigation to the order overview
  await expect(page).toHaveURL(/checkout-step-two\.html/);

  // Verify the order contains the expected product
  await expect(
    page.getByText('Sauce Labs Backpack', { exact: true })
  ).toBeVisible();

  // Verify the order summary
  await expect(page.getByText('Item total: $29.99')).toBeVisible();
});

test('TC_014 - Complete purchase and verify order confirmation', async ({ page }) => {
  const loginPage = new LoginPage(page);
  const inventoryPage = new InventoryPage(page);
  const cartPage = new CartPage(page);
  const checkoutPage = new CheckoutPage(page);

  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  // Add product and navigate to checkout
  await inventoryPage.addBackpackToCart();
  await page.locator('.shopping_cart_link').click();
  await cartPage.checkoutButton.click();

  // Submit checkout information
  await checkoutPage.enterCheckoutInformation(
    'Mridul',
    'Tripathi',
    '226001'
  );
  await checkoutPage.continueToOverview();

  // Verify order overview before placing the order
  await expect(page).toHaveURL(/checkout-step-two\.html/);
  await expect(
    page.getByText('Item total: $29.99')
  ).toBeVisible();

  // Complete the purchase
  await checkoutPage.finishCheckout();

  // Verify order confirmation
  await expect(page).toHaveURL(/checkout-complete\.html/);
  await expect(checkoutPage.confirmationMessage).toBeVisible();
  await expect(checkoutPage.backHomeButton).toBeVisible();
});