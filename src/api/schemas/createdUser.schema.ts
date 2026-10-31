
export const createdUserSchema = {
  type: 'object',
  properties: {
    id: { type: 'integer' },
    name: { type: 'string' },
    username: { type: 'string' },
    email: { type: 'string', format: 'email' },
  },
  required: ['id', 'name', 'username', 'email'],
  additionalProperties: true,
};
