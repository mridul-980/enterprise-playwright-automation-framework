# Enterprise Hybrid Automation Framework

A scalable, maintainable UI and API test automation framework built using **Playwright, TypeScript, and Playwright Test**. The framework supports cross-browser UI automation, API testing, reusable test architecture, schema validation, automated reporting, and CI/CD integration.

## 1. Technology Stack

| Technology | Purpose |
|---|---|
| Playwright Test | UI and API automation |
| TypeScript | Type-safe test development |
| Node.js & npm | Runtime and dependency management |
| Ajv | JSON schema validation |
| Allure Reports | Detailed test reporting |
| Playwright HTML Reporter | Execution reports |
| GitHub Actions | CI/CD automation |
| JSON Reporter | Machine-readable test results |

## 2. Key Features

- **Cross-browser UI automation:** Chromium and Firefox.
- **API automation:** GET, POST, PUT, and DELETE request validation.
- **Page Object Model:** Maintainable and reusable page classes.
- **Custom fixtures:** Reusable page objects and authenticated test setup.
- **Environment management:** Configurable local, QA, and staging environment names.
- **Reusable API clients:** Centralized API request operations.
- **Schema validation:** JSON response contract validation using Ajv.
- **Test data management:** Centralized API test payloads.
- **Test categorization:** Smoke and regression execution using tags.
- **Failure diagnostics:** Screenshots, traces, and videos on failure.
- **Multiple reports:** Playwright HTML, Allure, and JSON.
- **CI/CD integration:** Automated execution through GitHub Actions.

## 3. Project Architecture

```text
enterprise-playwright-automation-framework/
│
├── .github/
│   └── workflows/
│       └── playwright.yml
│
├── src/
│   ├── api/
│   │   ├── clients/
│   │   │   └── userApiClient.ts
│   │   ├── helpers/
│   │   │   └── schemaValidator.ts
│   │   ├── schemas/
│   │   │   ├── user.schema.ts
│   │   │   └── createdUser.schema.ts
│   │   └── types/
│   │       └── user.types.ts
│   │
│   ├── config/
│   │   └── environment.ts
│   │
│   ├── fixtures/
│   │   └── testFixtures.ts
│   │
│   ├── pages/
│   │   ├── BasePage.ts
│   │   ├── LoginPage.ts
│   │   ├── InventoryPage.ts
│   │   ├── CartPage.ts
│   │   └── CheckoutPage.ts
│   │
│   └── utils/
│
├── tests/
│   ├── ui/
│   │   └── inventory/
│   │
│   └── api/
│       └── users/
│           ├── users.spec.ts
│           └── user-schema.spec.ts
│
├── test-data/
│   └── api/
│       └── userTestData.ts
│
├── playwright.config.ts
├── package.json
├── package-lock.json
├── tsconfig.json
└── README.md
```

*Note: Generated reports, test results, and other ignored files are not shown. Update this illustrative tree if your current repository contains additional files or different test module names.*

### Architecture Overview

- **Pages:** Encapsulate UI locators and page-specific actions.
- **Fixtures:** Manage reusable page objects and common test setup.
- **API Clients:** Centralize API request operations.
- **Helpers:** Provide reusable schema validation.
- **Schemas:** Define expected API response structures.
- **Types:** Define TypeScript API request models.
- **Config:** Manage execution environments and base URLs.
- **Tests:** Separate UI and API test suites.
- **Test Data:** Store reusable test payloads independently of test logic.

## 4. Prerequisites

Install the following:

- Node.js (LTS recommended)
- npm
- Git
- Java 21 (for Allure report generation and opening)
- Allure CLI (for the configured Allure report commands)

## 5. Project Setup

Clone the repository:

```bash
git clone <repository-url>
cd enterprise-playwright-automation-framework
```

Install dependencies:

```bash
npm ci
```

Install Playwright browsers:

```bash
npx playwright install
```

For Linux environments, install browser system dependencies when required:

```bash
npx playwright install --with-deps
```

Validate TypeScript:

```bash
npm run typecheck
```

## 6. Environment Management

Environment configuration is maintained in:

```text
src/config/environment.ts
```

The framework supports three environment names:

| Environment | Default UI URL |
|---|---|
| local | https://www.saucedemo.com |
| qa | https://www.saucedemo.com |
| staging | https://www.saucedemo.com |

Currently, all three environment names use SauceDemo. They can be configured with independent URLs when separate application deployments become available.

The API base URL defaults to:

```text
https://jsonplaceholder.typicode.com
```

### Environment Variables

| Variable | Purpose |
|---|---|
| `ENV` | Selects the execution environment |
| `BASE_URL` | Overrides the UI base URL |
| `API_BASE_URL` | Overrides the API base URL |

### Select an Environment

Default environment: `local`.

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

### Override UI and API URLs

**Windows PowerShell**

```powershell
$env:ENV = "qa"
$env:BASE_URL = "https://www.saucedemo.com"
$env:API_BASE_URL = "https://jsonplaceholder.typicode.com"

npm test
```

**macOS/Linux**

```bash
ENV=qa BASE_URL=https://www.saucedemo.com API_BASE_URL=https://jsonplaceholder.typicode.com npm test
```

An unsupported `ENV` value causes configuration validation to fail.

## 7. Test Execution

### Run the Complete Test Suite

```bash
npm test
```

This executes all configured Playwright projects:

- Chromium UI
- Firefox UI
- API

### Run Browser-Specific Tests

```bash
npm run test:chromium
```

```bash
npm run test:firefox
```

### Run Smoke Tests

```bash
npm run test:smoke
```

### Run Regression Tests

```bash
npm run test:regression
```

### Run TypeScript Validation

```bash
npm run typecheck
```

### Test Execution Strategy

| Project | Test Coverage |
|---|---|
| Chromium | UI tests |
| Firefox | UI tests |
| API | API integration and schema validation tests |

Smoke and regression tests are selected using Playwright test tags.

## 8. API Automation

API automation uses Playwright's built-in `APIRequestContext`.

The framework includes:

- GET user details.
- POST user creation response validation.
- PUT user update response validation.
- DELETE response validation.
- Negative API testing.
- HTTP status and response-header validation.
- JSON schema validation using Ajv.
- Reusable API client architecture.
- Type-safe request payloads.

**Important:** JSONPlaceholder is a practice API. Its POST, PUT, and DELETE endpoints simulate responses and do not provide persistent data modification.

## 9. Reporting

The framework supports three reporting formats.

### Playwright HTML Report

Open the generated HTML report:

```bash
npm run report:playwright
```

### Allure Report

Generate the report:

```bash
npm run report:allure:generate
```

Open the report:

```bash
npm run report:allure:open
```

Allure results are generated during test execution and stored in:

```text
allure-results/
```

### JSON Report

The Playwright JSON reporter generates:

```text
playwright-report.json
```

This report is also used by the GitHub Actions workflow to publish a test summary.

## 10. Failure Diagnostics

The Playwright configuration captures diagnostic information when tests fail.

| Diagnostic | Configuration |
|---|---|
| Screenshots | `only-on-failure` |
| Trace | `retain-on-failure` |
| Video | `retain-on-failure` |

Generated execution artifacts are stored under:

```text
test-results/
```

These artifacts help investigate failed tests, execution errors, and flaky behavior.

## 11. CI/CD Integration

The framework is integrated with **GitHub Actions**.

Workflow file:

```text
.github/workflows/playwright.yml
```

### Pipeline Triggers

- Push to `main` or `master`.
- Pull requests targeting `main` or `master`.

### Pipeline Execution Flow

1. Checkout repository.
2. Set up Node.js.
3. Set up Java 21.
4. Install dependencies using `npm ci`.
5. Run TypeScript type checking.
6. Execute all Playwright projects.
7. Publish the Playwright test summary.
8. Generate the Allure report when results are available.
9. Upload execution reports and diagnostic artifacts.

### CI/CD Artifacts

The workflow uploads:

- Playwright HTML report.
- Allure results.
- Allure HTML report.
- Test diagnostics.

Artifacts are configured with a 30-day retention period.

## 12. Available npm Scripts

| Command | Description |
|---|---|
| `npm test` | Run all configured projects |
| `npm run test:chromium` | Run Chromium UI tests |
| `npm run test:firefox` | Run Firefox UI tests |
| `npm run test:smoke` | Run smoke-tagged tests |
| `npm run test:regression` | Run regression-tagged tests |
| `npm run typecheck` | Validate TypeScript |
| `npm run report:playwright` | Open Playwright HTML report |
| `npm run report:allure:generate` | Generate Allure report |
| `npm run report:allure:open` | Open Allure report |

## 13. Current Framework Validation

The latest validated suite contains:

| Project | Executions |
|---|---:|
| Chromium UI | 24 |
| Firefox UI | 24 |
| API | 9 |
| **Total** | **57** |

The framework has successfully completed GitHub Actions pipeline validation following the Sprint 26 improvements.

## 14. Engineering Practices

The framework follows these practices:

- Page Object Model for UI maintainability.
- Reusable Playwright fixtures.
- Separation of UI and API automation.
- Type-safe API request models.
- Centralized environment configuration.
- Reusable API clients.
- JSON schema validation.
- Centralized test data.
- Failure-focused diagnostics.
- Automated CI/CD execution.

---

**Enterprise Hybrid Automation Framework**  
Built with Playwright, TypeScript, and a focus on maintainable, scalable test automation.

**Created and maintained by Mridul Tripathi | SDET**