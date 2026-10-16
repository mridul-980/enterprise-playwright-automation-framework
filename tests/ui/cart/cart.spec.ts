import { test, expect } from '@playwright/test';
import { LoginPage } from '../../../src/pages/LoginPage';
import { InventoryPage } from '../../../src/pages/InventoryPage';
import { CartPage } from '../../../src/pages/CartPage';

test('TC_009 - Verify cart contents after adding a product', async ({ page }) => {
  const loginPage = new LoginPage(page);
  const inventoryPage = new InventoryPage(page);
  const cartPage = new CartPage(page);

  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await inventoryPage.addBackpackToCart();

  await page.locator('.shopping_cart_link').click();

  await expect(page).toHaveURL(/cart.html/);
  await expect(cartPage.cartItems).toHaveCount(1);
  await expect(cartPage.backpackName).toBeVisible();
  await expect(cartPage.checkoutButton).toBeVisible();
});

test('TC_010 - Verify product details and price in cart', async ({ page }) => {
  const loginPage = new LoginPage(page);
  const inventoryPage = new InventoryPage(page);
  const cartPage = new CartPage(page);

  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await inventoryPage.addBackpackToCart();
  await page.locator('.shopping_cart_link').click();

  await expect(cartPage.backpackName).toBeVisible();
  await expect(cartPage.backpackPrice).toHaveText('$29.99');
  await expect(cartPage.backpackQuantity).toHaveText('1');
});

test('TC_011 - Remove a product directly from cart', async ({ page }) => {
  const loginPage = new LoginPage(page);
  const inventoryPage = new InventoryPage(page);
  const cartPage = new CartPage(page);

  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await inventoryPage.addBackpackToCart();
  await page.locator('.shopping_cart_link').click();

  await expect(cartPage.cartItems).toHaveCount(1);

  await cartPage.removeBackpack();

  // Verify the cart is empty
  await expect(cartPage.cartItems).toHaveCount(0);

  // Verify the cart badge is removed
  await expect(page.locator('.shopping_cart_badge')).toHaveCount(0);

  // Verify checkout is still available
  await expect(cartPage.checkoutButton).toBeVisible();
});