import { test, expect } from '../../../src/fixtures/testFixtures';

test('TC_009 - Verify cart contents after adding a product @regression', async ({
  page,
  inventoryPage,
  cartPage,
  loggedIn,
}) => {
  await inventoryPage.addBackpackToCart();
  await page.locator('.shopping_cart_link').click();

  await expect(page).toHaveURL(/cart.html/);
  await expect(cartPage.cartItems).toHaveCount(1);
  await expect(cartPage.backpackName).toBeVisible();
  await expect(cartPage.checkoutButton).toBeVisible();
});

test('TC_010 - Verify product details and price in cart @regression', async ({
  page,
  inventoryPage,
  cartPage,
  loggedIn,
}) => {
  await inventoryPage.addBackpackToCart();
  await page.locator('.shopping_cart_link').click();

  await expect(cartPage.backpackName).toBeVisible();
  await expect(cartPage.backpackPrice).toHaveText('$29.99');
  await expect(cartPage.backpackQuantity).toHaveText('1');
});

test('TC_011 - Remove a product directly from cart @regression', async ({
  page,
  loggedIn,
  inventoryPage,
  cartPage,
}) => {

  await inventoryPage.addBackpackToCart();
  await page.locator('.shopping_cart_link').click();

  await expect(cartPage.cartItems).toHaveCount(1);
  await cartPage.removeBackpack();

  await expect(cartPage.cartItems).toHaveCount(0);
  await expect(page.locator('.shopping_cart_badge')).toHaveCount(0);
  await expect(cartPage.checkoutButton).toBeVisible();
});

test('TC_019 - Remove one product from a cart with multiple products @regression', async ({
  page,
  loggedIn,
  inventoryPage,
  cartPage,
}) => {

  await inventoryPage.addBackpackToCart();
  await inventoryPage.addBikeLightToCart();

  await page.locator('.shopping_cart_link').click();

  await expect(cartPage.cartItems).toHaveCount(2);
  await expect(page.locator('.shopping_cart_badge')).toHaveText('2');

  await cartPage.removeBackpack();

  await expect(cartPage.cartItems).toHaveCount(1);
  await expect(cartPage.backpackName).toHaveCount(0);
  await expect(cartPage.bikeLightName).toBeVisible();
  await expect(page.locator('.shopping_cart_badge')).toHaveText('1');
});
