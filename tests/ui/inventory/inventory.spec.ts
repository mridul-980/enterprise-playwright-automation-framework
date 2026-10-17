import { test, expect } from '../../../src/fixtures/testFixtures';

test('TC_005 - Verify inventory page after successful login', async ({
  page,
  loginPage,
  inventoryPage,
}) => {
  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await expect(page).toHaveURL(/inventory.html/);
  await expect(inventoryPage.productsHeading).toBeVisible();
  await expect(inventoryPage.inventoryItems.first()).toBeVisible();
  await expect(inventoryPage.inventoryItems).toHaveCount(6);
});

test('TC_006 - Verify product details on inventory page', async ({
  page,
  loginPage,
  inventoryPage,
}) => {
  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await expect(inventoryPage.backpackName).toBeVisible();
  await expect(inventoryPage.backpackPrice).toHaveText('$29.99');
  await expect(inventoryPage.backpackAddToCartButton).toBeVisible();
});

test('TC_007 - Add a product to cart and verify cart badge', async ({
  page,
  loginPage,
  inventoryPage,
}) => {
  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await inventoryPage.addBackpackToCart();

  await expect(page.locator('.shopping_cart_badge')).toHaveText('1');
  await expect(
    page.getByRole('button', { name: 'Remove' })
  ).toBeVisible();
});

test('TC_008 - Remove a product from cart', async ({
  page,
  loginPage,
  inventoryPage,
}) => {
  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await inventoryPage.addBackpackToCart();
  await expect(page.locator('.shopping_cart_badge')).toHaveText('1');

  await inventoryPage.removeBackpackFromCart();

  await expect(page.locator('.shopping_cart_badge')).toHaveCount(0);
  await expect(inventoryPage.backpackAddToCartButton).toBeVisible();
});