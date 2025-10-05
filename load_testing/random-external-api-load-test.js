import http from 'k6/http';
import { check, sleep } from 'k6';

const ENDPOINT_URL = __ENV.ENDPOINT_URL || 'http://192.168.1.139:30002/api/external/random';

export const options = {
  vus: 10,
  duration: '30s',
};

export default function () {
  const response = http.get(ENDPOINT_URL);

  check(response, {
    'status is 200': (res) => res.status === 200,
  });

  sleep(1);
}
