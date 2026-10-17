import { test, expect } from '../../../src/fixtures/testFixtures';

test('TC_009 - Verify cart contents after adding a product', async ({
  page,
  loginPage,
  inventoryPage,
  cartPage,
}) => {
  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await inventoryPage.addBackpackToCart();
  await page.locator('.shopping_cart_link').click();

  await expect(page).toHaveURL(/cart.html/);
  await expect(cartPage.cartItems).toHaveCount(1);
  await expect(cartPage.backpackName).toBeVisible();
  await expect(cartPage.checkoutButton).toBeVisible();
});

test('TC_010 - Verify product details and price in cart', async ({
  page,
  loginPage,
  inventoryPage,
  cartPage,
}) => {
  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await inventoryPage.addBackpackToCart();
  await page.locator('.shopping_cart_link').click();

  await expect(cartPage.backpackName).toBeVisible();
  await expect(cartPage.backpackPrice).toHaveText('$29.99');
  await expect(cartPage.backpackQuantity).toHaveText('1');
});

test('TC_011 - Remove a product directly from cart', async ({
  page,
  loginPage,
  inventoryPage,
  cartPage,
}) => {
  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await inventoryPage.addBackpackToCart();
  await page.locator('.shopping_cart_link').click();

  await expect(cartPage.cartItems).toHaveCount(1);
  await cartPage.removeBackpack();

  await expect(cartPage.cartItems).toHaveCount(0);
  await expect(page.locator('.shopping_cart_badge')).toHaveCount(0);
  await expect(cartPage.checkoutButton).toBeVisible();
});