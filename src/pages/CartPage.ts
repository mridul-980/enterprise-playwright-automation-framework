import { Locator, Page } from '@playwright/test';
import { BasePage } from './BasePage';

export class CartPage extends BasePage {
  readonly cartItems: Locator;
  readonly backpackName: Locator;
  readonly checkoutButton: Locator;
  readonly continueShoppingButton: Locator;
  readonly backpackPrice: Locator;
  readonly backpackQuantity: Locator;
  readonly bikeLightName: Locator;
  constructor(page: Page) {
    super(page);

    this.cartItems = page.locator('.cart_item');

    this.backpackName = page.getByText('Sauce Labs Backpack', {
      exact: true,
    });

    this.checkoutButton = page.getByRole('button', {
      name: 'Checkout',
    });

    this.continueShoppingButton = page.getByRole('button', {
      name: 'Continue Shopping',
    });
    this.backpackPrice = page.locator('.cart_item')
  .filter({ hasText: 'Sauce Labs Backpack' })
  .locator('.inventory_item_price');

    this.backpackQuantity = page.locator('.cart_item')
  .filter({ hasText: 'Sauce Labs Backpack' })
  .locator('.cart_quantity');

    this.bikeLightName = page.getByText('Sauce Labs Bike Light', {
      exact: true,
    });
  }
  async removeBackpack(): Promise<void> {
  await this.page.locator('.cart_item')
    .filter({ hasText: 'Sauce Labs Backpack' })
    .getByRole('button', { name: 'Remove' })
    .click();
}
}