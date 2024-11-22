```toml
name = 'cambioDispositivo_D2'
method = 'GET'
url = '{{baseUrl}}/api/catedra/cambio_d2'
sortWeight = 7000000
id = '62cd6cd2-72fd-4ba6-89cd-3e0ec1076b4e'

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
