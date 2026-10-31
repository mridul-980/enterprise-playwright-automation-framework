
export const userSchema = {
  type: 'object',
  properties: {
    id: { type: 'integer' },
    name: { type: 'string' },
    username: { type: 'string' },
    email: { type: 'string', format: 'email' },
    address: {
      type: 'object',
      properties: {
        street: { type: 'string' },
        suite: { type: 'string' },
        city: { type: 'string' },
        zipcode: { type: 'string' },
        geo: {
          type: 'object',
          properties: {
            lat: { type: 'string' },
            lng: { type: 'string' },
          },
          required: ['lat', 'lng'],
          additionalProperties: true,
        },
      },
      required: ['street', 'suite', 'city', 'zipcode', 'geo'],
      additionalProperties: true,
    },
    phone: { type: 'string' },
    website: { type: 'string' },
    company: {
      type: 'object',
      properties: {
        name: { type: 'string' },
        catchPhrase: { type: 'string' },
        bs: { type: 'string' },
      },
      required: ['name', 'catchPhrase', 'bs'],
      additionalProperties: true,
    },
  },
  required: [
    'id',
    'name',
    'username',
    'email',
    'address',
    'phone',
    'website',
    'company',
  ],
  additionalProperties: true,
};
