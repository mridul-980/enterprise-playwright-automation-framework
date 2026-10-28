import { test, expect } from '../../../src/fixtures/testFixtures';
import { validUser } from '../../../test-data/credentials';
import {
  checkoutUser,
  checkoutProduct,
} from '../../../test-data/checkoutData';

test('TC_012 - Navigate from cart to checkout @regression', async ({
  page,
  inventoryPage,
  cartPage,
  checkoutPage,
  loggedIn,
}) => {

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

test('TC_013 - Submit checkout information with valid details @regression', async ({
  page,
  loggedIn,
  inventoryPage,
  cartPage,
  checkoutPage,
}) => {

  await inventoryPage.addBackpackToCart();
  await page.locator('.shopping_cart_link').click();
  await cartPage.checkoutButton.click();

  await checkoutPage.enterCheckoutInformation(
      checkoutUser.firstName,
      checkoutUser.lastName,
      checkoutUser.postalCode
  );
  await checkoutPage.continueToOverview();

  await expect(page).toHaveURL(/checkout-step-two\.html/);
  await expect(
  page.getByText(checkoutProduct.name, { exact: true })
).toBeVisible();
  await expect(page.getByText(`Item total: ${checkoutProduct.itemTotal}`)).toBeVisible();
});

test('TC_014 - Complete purchase and verify order confirmation @smoke @regression', async ({
  page,
  loggedIn,
  inventoryPage,
  cartPage,
  checkoutPage,
}) => {

  await inventoryPage.addBackpackToCart();
  await page.locator('.shopping_cart_link').click();
  await cartPage.checkoutButton.click();

  await checkoutPage.enterCheckoutInformation(
     checkoutUser.firstName,
     checkoutUser.lastName,
     checkoutUser.postalCode
  );
  await checkoutPage.continueToOverview();

  await expect(page).toHaveURL(/checkout-step-two\.html/);
  await expect(page.getByText(checkoutProduct.name, { exact: true })).toBeVisible();
  await expect(page.getByText(`Item total: ${checkoutProduct.itemTotal}`)).toBeVisible();

  await checkoutPage.finishCheckout();

  await expect(page).toHaveURL(/checkout-complete\.html/);
  await expect(checkoutPage.confirmationMessage).toBeVisible();
  await expect(checkoutPage.backHomeButton).toBeVisible();
});

test('TC_015 - Checkout with missing first name @regression', async ({
  loggedIn,
  inventoryPage,
  cartPage,
  checkoutPage,
}) => {

  await inventoryPage.addBackpackToCart();
  await cartPage.navigateTo('/cart.html');
  await cartPage.checkoutButton.click();

  await checkoutPage.enterCheckoutInformation(
    '',
    'Tripathi',
    '226001'
  );

  await checkoutPage.continueToOverview();

  await expect(checkoutPage.getErrorMessage()).toBeVisible();
  await expect(checkoutPage.getErrorMessage()).toHaveText(
    'Error: First Name is required'
  );
});

test('TC_016 - Checkout with missing last name @regression', async ({
  loggedIn,
  inventoryPage,
  cartPage,
  checkoutPage,
}) => {

  await inventoryPage.addBackpackToCart();
  await cartPage.navigateTo('/cart.html');
  await cartPage.checkoutButton.click();

  await checkoutPage.enterCheckoutInformation(
    'Mridul',
    '',
    '226001'
  );

  await checkoutPage.continueToOverview();

  await expect(checkoutPage.getErrorMessage()).toBeVisible();
  await expect(checkoutPage.getErrorMessage()).toHaveText(
    'Error: Last Name is required'
  );
});

test('TC_017 - Checkout with missing postal code @regression', async ({
  loggedIn,
  inventoryPage,
  cartPage,
  checkoutPage,
}) => {

  await inventoryPage.addBackpackToCart();
  await cartPage.navigateTo('/cart.html');
  await cartPage.checkoutButton.click();

  await checkoutPage.enterCheckoutInformation(
    'Mridul',
    'Tripathi',
    ''
  );

  await checkoutPage.continueToOverview();

  await expect(checkoutPage.getErrorMessage()).toBeVisible();
  await expect(checkoutPage.getErrorMessage()).toHaveText(
    'Error: Postal Code is required'
  );
});
