import http from 'k6/http';
import {sleep} from 'k6';

export const options = {
  stages: [
    {duration: "5s", target: 150}
  ],
};

export default function () {
  http.post('http://localhost:10020/tickets');
  sleep(1);
}
