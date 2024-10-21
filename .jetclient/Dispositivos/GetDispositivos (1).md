```toml
name = 'GetDispositivos (1)'
method = 'GET'
url = '{{baseUrl}}/api/dispositivos'
sortWeight = 2000000
id = '968622ba-635d-422e-a7f2-3292951da3fd'

[[queryParams]]
key = 'Content-Type'
value = 'application/json'
disabled = true

[[queryParams]]
key = 'Authorization'
value = 'Bearer {{token}}'
disabled = true

[[headers]]
key = 'Content-Type'
value = 'application/json'

[[headers]]
key = 'Authorization'
value = 'Bearer {{token}}'

[auth]
type = 'NO_AUTH'
```
