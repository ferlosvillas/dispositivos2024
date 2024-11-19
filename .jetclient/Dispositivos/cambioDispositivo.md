```toml
name = 'cambioDispositivo'
method = 'GET'
url = '{{baseUrl}}/api/catedra/cambio_d1'
sortWeight = 6000000
id = 'c62ebadf-0f3e-4791-8c6d-2e84a7c94d1a'

[[headers]]
key = 'Content-Type'
value = 'application/json'
disabled = true

[[headers]]
key = 'Authorization'
value = 'Bearer {{token}}'

[auth]
type = 'NO_AUTH'
```
