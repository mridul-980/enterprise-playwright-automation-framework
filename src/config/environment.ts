
const environments = {
  local: 'https://www.saucedemo.com',
  qa: 'https://www.saucedemo.com',
  staging: 'https://www.saucedemo.com',
} as const;

type EnvironmentName = keyof typeof environments;

const selectedEnvironment = (
  process.env.ENV ?? 'local'
).toLowerCase();

if (
  !Object.prototype.hasOwnProperty.call(
    environments,
    selectedEnvironment,
  )
) {
  throw new Error(
    `Invalid ENV: "${selectedEnvironment}". ` +
      'Allowed values: local, qa, staging.',
  );
}

const environmentName =
  selectedEnvironment as EnvironmentName;

export const environment = {
  name: environmentName,
  baseURL:
    process.env.BASE_URL ?? environments[environmentName],
};
