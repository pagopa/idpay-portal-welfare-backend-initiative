# idpay-portal-welfare-backend-initiative
This project services the purpose of handling the initiative lifecycle in the IDPay Environment

## How test helm template

```sh
 helm dep build && helm template . -f values-dev.yaml  --debug
```

## How deploy helm

```sh
helm dep build && helm upgrade --namespace idpay --install --values values-dev.yaml --wait --timeout 5m0s idpay-portal-welfare-backend-initiative .
```