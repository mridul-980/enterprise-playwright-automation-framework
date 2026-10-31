
import Ajv, { type AnySchema } from 'ajv';
import addFormats from 'ajv-formats';

const ajv = new Ajv({
  allErrors: true,
});

addFormats(ajv);

export function validateSchema(
  schema: AnySchema,
  responseBody: unknown
): void {
  const validate = ajv.compile(schema);
  const isValid = validate(responseBody);

  if (!isValid) {
    throw new Error(
      `Schema validation failed:\n${ajv.errorsText(validate.errors, {
        separator: '\n',
      })}`
    );
  }
}
