import { test, expect } from '../../../src/fixtures/testFixtures';

test('TC_005 - Verify inventory page after successful login @smoke @regression', async ({
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

test('TC_006 - Verify product details on inventory page @regression', async ({
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

test('TC_007 - Add a product to cart and verify cart badge @smoke @regression', async ({
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

test('TC_008 - Remove a product from cart @regression', async ({
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

test('TC_018 - Add multiple products and verify cart contents @regression', async ({
  page,
  loginPage,
  inventoryPage,
  cartPage,
}) => {
  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await inventoryPage.addBackpackToCart();
  await inventoryPage.addBikeLightToCart();

  await expect(page.locator('.shopping_cart_badge')).toHaveText('2');

  await page.locator('.shopping_cart_link').click();

  await expect(cartPage.cartItems).toHaveCount(2);
  await expect(cartPage.backpackName).toBeVisible();
  await expect(cartPage.bikeLightName).toBeVisible();
});

test('TC_020 - Sort products by price low to high @regression', async ({
  loginPage,
  inventoryPage,
}) => {
  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await inventoryPage.sortProducts('Price (low to high)');

  const prices = await inventoryPage.inventoryItemPrices.allTextContents();
  const numericPrices = prices.map(price =>
    Number(price.replace('$', ''))
  );

  expect(numericPrices).toEqual(
    [...numericPrices].sort((a, b) => a - b)
  );
});

test('TC_021 - Sort products by price high to low @regression', async ({
  loginPage,
  inventoryPage,
}) => {
  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await inventoryPage.sortProducts('Price (high to low)');

  const prices = await inventoryPage.inventoryItemPrices.allTextContents();
  const numericPrices = prices.map(price =>
    Number(price.replace('$', ''))
  );

  expect(numericPrices).toEqual(
    [...numericPrices].sort((a, b) => b - a)
  );
});

test('TC_022 - Sort products by name A to Z @regression', async ({
  loginPage,
  inventoryPage,
}) => {
  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await inventoryPage.sortProducts('Name (A to Z)');

  const names = await inventoryPage.inventoryItemNames.allTextContents();

  expect(names).toEqual(
    [...names].sort((a, b) => a.localeCompare(b))
  );
});


test('TC_023 - Sort products by name Z to A @regression', async ({
  loginPage,
  inventoryPage,
}) => {
  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await inventoryPage.sortProducts('Name (Z to A)');

  const names =
    await inventoryPage.inventoryItemNames.allTextContents();

  expect(names).toEqual(
    [...names].sort((a, b) => b.localeCompare(a))
  );
});

test('TC_024 - Sorting inventory preserves cart state @regression', async ({
  page,
  loginPage,
  inventoryPage,
  cartPage,
}) => {
  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await inventoryPage.addBackpackToCart();
  await inventoryPage.sortProducts('Price (low to high)');

  await expect(page.locator('.shopping_cart_badge')).toHaveText('1');

  await page.locator('.shopping_cart_link').click();

  await expect(cartPage.cartItems).toHaveCount(1);
  await expect(cartPage.backpackName).toBeVisible();
});
