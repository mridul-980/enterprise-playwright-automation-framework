# Enterprise Hybrid Automation Framework

A scalable test automation framework built with **Playwright, TypeScript, and Playwright Test**, with automated reporting and configurable execution environments.

## 1. Technology Stack

- Playwright Test
- TypeScript
- Node.js and npm
- Allure Reports
- Playwright HTML Reports

## 2. Prerequisites

Install the following before running the framework:

- Node.js (LTS recommended)
- npm
- Git (if cloning the repository)

## 3. Project Setup

Install project dependencies:

```bash
npm install
```

Install the required Playwright browsers:

```bash
npx playwright install
```

Run TypeScript validation:

```bash
npm run typecheck
```

## 4. Environment Management

The framework supports three execution environment names:

| Environment | Default URL |
|---|---|
| `local` | `https://www.saucedemo.com` |
| `qa` | `https://www.saucedemo.com` |
| `staging` | `https://www.saucedemo.com` |

All three currently point to SauceDemo. They can be assigned different URLs when separate application deployments become available.

### Select an environment

The default environment is `local`.

**Windows PowerShell**

```powershell
$env:ENV = "local"
npm test
```

```powershell
$env:ENV = "qa"
npm test
```

```powershell
$env:ENV = "staging"
npm test
```

**macOS/Linux**

```bash
ENV=local npm test
ENV=qa npm test
ENV=staging npm test
```

### Override the base URL

The `BASE_URL` environment variable takes precedence over the URL configured for `ENV`.

Windows PowerShell:

```powershell
$env:ENV = "qa"
$env:BASE_URL = "https://www.saucedemo.com"
npm test
```

macOS/Linux:

```bash
ENV=qa BASE_URL=https://www.saucedemo.com npm test
```

An unsupported `ENV` value causes configuration validation to fail.

## 5. Test Execution

Run the complete test suite:

```bash
npm test
```

Run tests using the Chromium project:

```bash
npm run test:chromium
```

The Playwright configuration currently defines Chromium and Firefox projects.

## 6. Reports

### Playwright HTML Report

Tests generate a Playwright HTML report.

Open the latest report:

```bash
npm run report:playwright
```

### Allure Report

Generate the Allure report from the test results:

```bash
npm run report:allure:generate
```

Open the generated report:

```bash
npm run report:allure:open
```

## 7. Available npm Scripts

| Command | Purpose |
|---|---|
| `npm test` | Run the complete test suite |
| `npm run test:chromium` | Run tests in Chromium |
| `npm run typecheck` | Validate TypeScript types |
| `npm run report:playwright` | Open the Playwright HTML report |
| `npm run report:allure:generate` | Generate the Allure report |
| `npm run report:allure:open` | Open the Allure report |

## 8. Configuration

Environment configuration is maintained in:

```text
src/config/environment.ts
```

Playwright configuration is maintained in:

```text
playwright.config.ts
```

The Playwright configuration uses the selected environment's `baseURL` as the shared browser-test base URL.

## 9. Regression Validation

After framework configuration changes, validate the TypeScript configuration and run the regression suite:

```bash
npm run typecheck
npm test
```

**Latest Sprint 11 regression result:** 34 tests passed.

## 10. Future Enhancements

- Configure independent URLs for local, QA, and staging deployments.
- Integrate execution with CI/CD pipelines.
- Expand cross-browser and parallel execution coverage.
- Enhance reporting, diagnostics, and failure analysis.
