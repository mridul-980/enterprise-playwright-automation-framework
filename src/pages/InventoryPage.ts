import { Page, Locator } from '@playwright/test';
import { BasePage } from './BasePage';

export class InventoryPage extends BasePage {
  readonly productsHeading: Locator;
  readonly inventoryItems: Locator;
  readonly backpackName: Locator;
  readonly backpackPrice: Locator;
  readonly backpackAddToCartButton: Locator;
  readonly bikeLightName: Locator;
  readonly bikeLightAddToCartButton: Locator;

  constructor(page: Page) {
    super(page);

    this.productsHeading = page.getByText('Products', {
      exact: true,
    });

    this.inventoryItems = page.locator('.inventory_item');

    this.backpackName = page.getByText('Sauce Labs Backpack', {
      exact: true,
    });

    this.backpackPrice = page.locator('.inventory_item')
      .filter({ hasText: 'Sauce Labs Backpack' })
      .locator('.inventory_item_price');

    this.backpackAddToCartButton = page.locator('.inventory_item')
      .filter({ hasText: 'Sauce Labs Backpack' })
      .getByRole('button', { name: 'Add to cart' });

    this.bikeLightName = page.getByText('Sauce Labs Bike Light', {
      exact: true,
});

    this.bikeLightAddToCartButton = page.locator('.inventory_item')
      .filter({ hasText: 'Sauce Labs Bike Light' })
      .getByRole('button', { name: 'Add to cart' });
  }
  async addBackpackToCart(): Promise<void> {
  await this.backpackAddToCartButton.click();
}
  async removeBackpackFromCart(): Promise<void> {
  await this.page.locator('.inventory_item')
    .filter({ hasText: 'Sauce Labs Backpack' })
    .getByRole('button', { name: 'Remove' })
    .click();
}
async addBikeLightToCart(): Promise<void> {
  await this.bikeLightAddToCartButton.click();
}
}