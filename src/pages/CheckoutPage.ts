import { Locator, Page } from '@playwright/test';
import { BasePage } from './BasePage';

export class CheckoutPage extends BasePage {
  readonly firstNameInput: Locator;
  readonly lastNameInput: Locator;
  readonly postalCodeInput: Locator;
  readonly continueButton: Locator;
  readonly finishButton: Locator;
  readonly checkoutTitle: Locator;
  readonly confirmationMessage: Locator;
  readonly backHomeButton: Locator;

  constructor(page: Page) {
    super(page);

    this.firstNameInput = page.getByPlaceholder('First Name');
    this.lastNameInput = page.getByPlaceholder('Last Name');
    this.postalCodeInput = page.getByPlaceholder('Zip/Postal Code');

    this.continueButton = page.getByRole('button', {
      name: 'Continue',
    });

    this.finishButton = page.getByRole('button', {
      name: 'Finish',
    });

    this.checkoutTitle = page.getByText('Checkout: Your Information', {
      exact: true,
    });
    this.confirmationMessage = page.getByText(
  'Thank you for your order!',
  { exact: true }
);

this.backHomeButton = page.getByRole('button', {
  name: 'Back Home',
});
  }
  async enterCheckoutInformation(
  firstName: string,
  lastName: string,
  postalCode: string
): Promise<void> {
  await this.firstNameInput.fill(firstName);
  await this.lastNameInput.fill(lastName);
  await this.postalCodeInput.fill(postalCode);
}

async continueToOverview(): Promise<void> {
  await this.continueButton.click();
}
async finishCheckout(): Promise<void> {
  await this.finishButton.click();
}
}